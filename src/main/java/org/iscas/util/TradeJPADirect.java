package org.iscas.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import org.iscas.entity.*;

public class TradeJPADirect implements TradeServices, TradeDBServices {

    @PersistenceUnit(unitName="daytrader")
    private static EntityManagerFactory emf;

    private static BigDecimal ZERO = new BigDecimal(0.0);

    private static boolean initialized = false;
    
    private Integer soldholdingID = null;
    private Object soldholdingIDlock = new Object();

    /**
     * Zero arg constructor for TradeJPADirect
     */
    public TradeJPADirect() {


        // FIXME - Why is this here???
        TradeConfig.setPublishQuotePriceChange(false);
        if (emf == null) {
            Log.error("TradeJPADirect.ctor:  Calling createEntityManagerFactory()");
            // creating entity manager factory. the persistence xml must be
            // place under src/META-INF/
            emf = Persistence.createEntityManagerFactory("daytrader");
        }

        if (initialized == false)
            init();
    }

    public void setEmf (EntityManagerFactory em) { 
        emf = em;
    }

    public static synchronized void init() {
        if (initialized)
            return;
        if (Log.doTrace())
            Log.trace("TradeJPADirect:init -- *** initializing");  

        TradeConfig.setPublishQuotePriceChange(false);

        if (Log.doTrace())
            Log.trace("TradeJPADirect:init -- +++ initialized");

        initialized = true;
    }

    public static void destroy() {
        try {
            if (!initialized)
                return;
            Log.trace("TradeJPADirect:destroy");
        }
        catch (Exception e) {
            Log.error("TradeJPADirect:destroy", e);
        }

    }

    public MarketSummaryDataBean getMarketSummary() {
        MarketSummaryDataBean marketSummaryData;

        /*
         * Creating entiManager
         */
        EntityManager entityManager = emf.createEntityManager();

        try {
            if (Log.doTrace())
                Log.trace("TradeJPADirect:getMarketSummary -- getting market summary");

            // Find Trade Stock Index Quotes (Top 100 quotes)
            // ordered by their change in value
            Collection<Quote> quotes;

            Query query = entityManager
                          .createNamedQuery("quote.quotesByChange");
            quotes = query.getResultList();

            Quote[] quoteArray = (Quote[]) quotes.toArray(new Quote[quotes.size()]);
            ArrayList<Quote> topGainers = new ArrayList<Quote>(5);
            ArrayList<Quote> topLosers = new ArrayList<Quote>(5);
            BigDecimal TSIA = FinancialUtils.ZERO;
            BigDecimal openTSIA = FinancialUtils.ZERO;
            double totalVolume = 0.0;

            if (quoteArray.length > 5) {
                for (int i = 0; i < 5; i++)
                    topGainers.add(quoteArray[i]);
                for (int i = quoteArray.length - 1; i >= quoteArray.length - 5; i--)
                    topLosers.add(quoteArray[i]);

                for (Quote quote : quoteArray) {
                    BigDecimal price = quote.getPrice();
                    BigDecimal open = quote.getOpen();
                    double volume = quote.getVolume();
                    TSIA = TSIA.add(price);
                    openTSIA = openTSIA.add(open);
                    totalVolume += volume;
                }
                TSIA = TSIA.divide(new BigDecimal(quoteArray.length),
                                   FinancialUtils.ROUND);
                openTSIA = openTSIA.divide(new BigDecimal(quoteArray.length),
                                           FinancialUtils.ROUND);
            }

            marketSummaryData = new MarketSummaryDataBean(TSIA, openTSIA,
                                                          totalVolume, topGainers, topLosers);
        }
        catch (Exception e) {
            Log.error("TradeJPADirect:getMarketSummary", e);
            throw new RuntimeException("TradeJPADirect:getMarketSummary -- error ", e);
        } finally {
            entityManager.close();
        }

        return marketSummaryData;
    }

    public Order buy(String userID, String symbol, double quantity, int orderProcessingMode) {
        Order order = null;
        BigDecimal total;
        /*
         * creating entitymanager
         */
        EntityManager entityManager = emf.createEntityManager();

        try {
            if (Log.doTrace())
                Log.trace("TradeJPADirect:buy", userID, symbol, quantity, orderProcessingMode);

            entityManager.getTransaction().begin();

            AccountProfile profile = entityManager.find(
                                                               AccountProfile.class, userID);
             Account account = profile.getAccount();

            Quote quote = entityManager.find(Quote.class,
                                                     symbol);

            Holding holding = null; // The holding will be created by this buy order

            order = createOrder(account, quote, holding, "buy", quantity, entityManager);

            // order = createOrder(account, quote, holding, "buy", quantity);
            // UPDATE - account should be credited during completeOrder

            BigDecimal price = quote.getPrice();
            BigDecimal orderFee = order.getOrderFee();
            BigDecimal balance = account.getBalance();
            total = (new BigDecimal(quantity).multiply(price)).add(orderFee);
            account.setBalance(balance.subtract(total));

            // commit the transaction before calling completeOrder
            entityManager.getTransaction().commit();

            if (orderProcessingMode == TradeConfig.SYNCH)
                completeOrder(order.getOrderID(), false);
            else if (orderProcessingMode == TradeConfig.ASYNCH_2PHASE)
                queueOrder(order.getOrderID(), true);
        }
        catch (Exception e) {
            Log.error("TradeJPADirect:buy(" + userID + "," + symbol + "," + quantity + ") --> failed", e);
            /* On exception - cancel the order */
            // TODO figure out how to do this with JPA
            if (order != null)
                order.cancel();

            entityManager.getTransaction().rollback();

            // throw new EJBException(e);
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }

        }

        // after the purchase or sell of a stock, update the stocks volume and
        // price
        updateQuotePriceVolume(symbol, TradeConfig.getRandomPriceChangeFactor(), quantity);

        return order;
    }

    public Order sell(String userID, Integer holdingID,
                              int orderProcessingMode) {
        EntityManager entityManager = emf.createEntityManager();

        Order order = null;
        BigDecimal total;
        try {
            entityManager.getTransaction().begin();
            if (Log.doTrace())
                Log.trace("TradeJPADirect:sell", userID, holdingID, orderProcessingMode);

            AccountProfile profile = entityManager.find(
                                                               AccountProfile.class, userID);

             Account account = profile.getAccount();
            Holding holding = entityManager.find(Holding.class,
                                                         holdingID);

            if (holding == null) {
                Log.error("TradeJPADirect:sell User " + userID
                          + " attempted to sell holding " + holdingID
                          + " which has already been sold");

                Order orderData = new Order();
                orderData.setOrderStatus("cancelled");

                entityManager.persist(orderData);
                entityManager.getTransaction().commit();
                return orderData;
            }

            Quote quote = holding.getQuote();
            double quantity = holding.getQuantity();

            order = createOrder(account, quote, holding, "sell", quantity,
                                entityManager);
            // UPDATE the holding purchase data to signify this holding is
            // "inflight" to be sold
            // -- could add a new holdingStatus attribute to holdingEJB
            holding.setPurchaseDate(new Timestamp(0));

            // UPDATE - account should be credited during completeOrder
            BigDecimal price = quote.getPrice();
            BigDecimal orderFee = order.getOrderFee();
            BigDecimal balance = account.getBalance();
            total = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);

            account.setBalance(balance.add(total));

            // commit the transaction before calling completeOrder
            entityManager.getTransaction().commit();

            if (orderProcessingMode == TradeConfig.SYNCH) {                
                synchronized(soldholdingIDlock) {
                    this.soldholdingID = holding.getHoldingID();
                    completeOrder(order.getOrderID(), false);
                }                
            } else if (orderProcessingMode == TradeConfig.ASYNCH_2PHASE)
                queueOrder(order.getOrderID(), true);

        }
        catch (Exception e) {
            Log.error("TradeJPADirect:sell(" + userID + "," + holdingID + ") --> failed", e);
            // TODO figure out JPA cancel
            if (order != null)
                order.cancel();

            entityManager.getTransaction().rollback();

            throw new RuntimeException("TradeJPADirect:sell(" + userID + "," + holdingID + ")", e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }

        if (!(order.getOrderStatus().equalsIgnoreCase("cancelled")))
            //after the purchase or sell of a stock, update the stocks volume and price
            updateQuotePriceVolume(order.getSymbol(), TradeConfig.getRandomPriceChangeFactor(), order.getQuantity());

        return order;
    }

    public void queueOrder(Integer orderID, boolean twoPhase) {
        Log
        .error("TradeJPADirect:queueOrder() not implemented for this runtime mode");
        throw new UnsupportedOperationException(
                                               "TradeJPADirect:queueOrder() not implemented for this runtime mode");
    }

    public Order completeOrder(Integer orderID, boolean twoPhase)
    throws Exception {
        EntityManager entityManager = emf.createEntityManager();
        Order order = null;

        if (Log.doTrace())
            Log.trace("TradeJPADirect:completeOrder", orderID + " twoPhase=" + twoPhase);

        order = entityManager.find(Order.class, orderID);
        order.getQuote();

        if (order == null) {
            Log.error("TradeJPADirect:completeOrder -- Unable to find Order " + orderID + " FBPK returned " + order);
            return null;
        }

        if (order.isCompleted()) {
            throw new RuntimeException("Error: attempt to complete Order that is already completed\n" + order);
        }

         Account account = order.getAccount();
        Quote quote = order.getQuote();
        Holding holding = null;
        if(order.isSell() && this.soldholdingID != null){
            holding = entityManager.find(Holding.class, this.soldholdingID);
        }        
        BigDecimal price = order.getPrice();
        double quantity = order.getQuantity();

        //String userID = account.getProfile().getUserID();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:completeOrder--> Completing Order "
                      + order.getOrderID() + "\n\t Order info: " + order
                      + "\n\t Account info: " + account + "\n\t Quote info: "
                      + quote + "\n\t Holding info: " + holding);

        Holding newHolding = null;        
        
        if (order.isBuy()) {
            /*
             * Complete a Buy operation - create a new Holding for the Account -
             * deduct the Order cost from the Account balance
             */
            //newHolding = createHolding(account, quote, quantity, price, entityManager);
            entityManager.getTransaction().begin();
            newHolding = new Holding(quantity, price, new Timestamp(System.currentTimeMillis()), account, quote);
            entityManager.persist(newHolding);            
            
            if (newHolding != null) {
                order.setHolding(newHolding);
            }
            entityManager.getTransaction().commit();
        }

        try {
            if (order.isSell()) {
                /*
                 * Complete a Sell operation - remove the Holding from the Account -
                 * deposit the Order proceeds to the Account balance
                 */
                if (holding == null) {
                    Log.error("TradeJPADirect:completeOrder -- Unable to sell order " + order.getOrderID() + " holding already sold");
                    order.cancel();                    
                    return order;
                }
                else {
                    entityManager.getTransaction().begin();
                    entityManager.remove(holding);                    
                    order.setHolding(null);
                    this.soldholdingID = null;
                    entityManager.getTransaction().commit();
                }
            }

            order.setOrderStatus("closed");

            order.setCompletionDate(new Timestamp(System.currentTimeMillis()));

            if (Log.doTrace())
                Log.trace("TradeJPADirect:completeOrder--> Completed Order "
                          + order.getOrderID() + "\n\t Order info: " + order
                          + "\n\t Account info: " + account + "\n\t Quote info: "
                          + quote + "\n\t Holding info: " + holding);

        }
        catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }

        return order;
    }

    public void cancelOrder(Integer orderID, boolean twoPhase) {
        EntityManager entityManager = emf.createEntityManager();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:cancelOrder", orderID + " twoPhase=" + twoPhase);

        Order order = entityManager.find(Order.class, orderID);
        /*
         * managed transaction
         */
        try {
            entityManager.getTransaction().begin();
            order.cancel();
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void orderCompleted(String userID, Integer orderID) {
        if (Log.doActionTrace())
            Log.trace("TradeAction:orderCompleted", userID, orderID);
        if (Log.doTrace())
            Log.trace("OrderCompleted", userID, orderID);
    }

    public Collection<Order> getOrders(String userID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getOrders", userID);
        EntityManager entityManager = emf.createEntityManager();
        AccountProfile profile = entityManager.find(
                                                           AccountProfile.class, userID);
         Account account = profile.getAccount();
        entityManager.close();
        return account.getOrders();
    }

    public Collection<Order> getClosedOrders(String userID) {

        if (Log.doTrace())
            Log.trace("TradeJPADirect:getClosedOrders", userID);
        EntityManager entityManager = emf.createEntityManager();

        try {

            // Get the primary keys for all the closed Orders for this
            // account.
            /*
             * managed transaction
             */
            //entityManager.getTransaction().begin();
            Query query = entityManager
                          .createNamedQuery("order.closedOrders");
            query.setParameter("userID", userID);

            //entityManager.getTransaction().commit();
            Collection results = query.getResultList();
            Iterator itr = results.iterator();
            // entityManager.joinTransaction();
            // Spin through the orders to populate the lazy quote fields
            while (itr.hasNext()) {
                Order thisOrder = (Order) itr.next();
                thisOrder.getQuote();
            }

            if (TradeConfig.jpaLayer == TradeConfig.OPENJPA) {
                Query updateStatus = entityManager
                                     .createNamedQuery("order.completeClosedOrders");
                /*
                 * managed transaction
                 */
                try {
                    entityManager.getTransaction().begin();
                    updateStatus.setParameter("userID", userID);

                    updateStatus.executeUpdate();
                    entityManager.getTransaction().commit();
                }
                catch (Exception e) {
                    entityManager.getTransaction().rollback();
                    entityManager.close();
                    entityManager = null;
                }
            }
            else if (TradeConfig.jpaLayer == TradeConfig.HIBERNATE) {
                try {
                /*
                 * Add logic to do update orders operation, because JBoss5'
                 * Hibernate 3.3.1GA DB2Dialect and MySQL5Dialect do not work
                 * with annotated query "order.completeClosedOrders" defined
                 * in Order
                 */
                Query findaccountid = entityManager
                                      .createNativeQuery(
                                                        "select "
                                                        + "a.ACCOUNTID, "
                                                        + "a.LOGINCOUNT, "
                                                        + "a.LOGOUTCOUNT, "
                                                        + "a.LASTLOGIN, "
                                                        + "a.CREATIONDATE, "
                                                        + "a.BALANCE, "
                                                        + "a.OPENBALANCE, "
                                                        + "a.PROFILE_USERID "
                                                        + "from account a where a.profile_userid = ?",
                                                         Account.class);
                findaccountid.setParameter(1, userID);
                 Account account = ( Account) findaccountid.getSingleResult();
                Integer accountid = account.getAccountID();
                Query updateStatus = entityManager.createNativeQuery("UPDATE order o SET o.orderStatus = 'completed' WHERE "
                                                                     + "o.orderStatus = 'closed' AND o.ACCOUNT_ACCOUNTID  = ?");
                updateStatus.setParameter(1, accountid.intValue());
                entityManager.getTransaction().begin();
                updateStatus.executeUpdate();
                entityManager.getTransaction().commit();
                } catch (Exception e){
                    entityManager.getTransaction().rollback();
                    entityManager.close();
                    entityManager = null;
                }
            }
            
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
            return results;
        }
        catch (Exception e) {
            Log.error("TradeJPADirect.getClosedOrders", e);
            entityManager.close();
            entityManager = null;
            throw new RuntimeException(
                                      "TradeJPADirect.getClosedOrders - error", e);

        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }

    }

    public Quote createQuote(String symbol, String companyName,
                                     BigDecimal price) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            Quote quote = new Quote(symbol, companyName, 0, price, price, price, price, 0);
            /*
             * managed transaction
             */
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(quote);
                entityManager.getTransaction().commit();
            }
            catch (Exception e) {
                entityManager.getTransaction().rollback();
            }

            if (Log.doTrace())
                Log.trace("TradeJPADirect:createQuote-->" + quote);

            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
            return quote;
        }
        catch (Exception e) {
            Log.error("TradeJPADirect:createQuote -- exception creating Quote", e);
            entityManager.close();
            entityManager = null;
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }

    }

    public Quote getQuote(String symbol) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getQuote", symbol);
        EntityManager entityManager = emf.createEntityManager();

        Quote qdb = entityManager.find(Quote.class, symbol);

        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }
        return qdb;
    }

    public Collection<Quote> getAllQuotes() {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getAllQuotes");
        EntityManager entityManager = emf.createEntityManager();

        Query query = entityManager.createNamedQuery("quote.allQuotes");

        if (entityManager != null) {
            entityManager.close();
            entityManager = null;

        }
        return query.getResultList();
    }

    public Quote updateQuotePriceVolume(String symbol,
                                                BigDecimal changeFactor, double sharesTraded) {
        if (!TradeConfig.getUpdateQuotePrices())
            return new Quote();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:updateQuote", symbol, changeFactor);

        /*
         * Add logic to determine JPA layer, because JBoss5' Hibernate 3.3.1GA
         * DB2Dialect and MySQL5Dialect do not work with annotated query
         * "quote.quoteForUpdate" defined in Quote
         */
        EntityManager entityManager = emf.createEntityManager();
        Quote quote = null;
        if (TradeConfig.jpaLayer == TradeConfig.HIBERNATE) {
            quote = entityManager.find(Quote.class, symbol);
        } else if (TradeConfig.jpaLayer == TradeConfig.OPENJPA) {
  
            Query q = entityManager.createNamedQuery("quote.quoteForUpdate");
            q.setParameter(1, symbol);
  
            quote = (Quote) q.getSingleResult();
        }

        BigDecimal oldPrice = quote.getPrice();

        if (quote.getPrice().equals(TradeConfig.PENNY_STOCK_PRICE)) {
            changeFactor = TradeConfig.PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER;
        }

        BigDecimal newPrice = changeFactor.multiply(oldPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

        /*
         * managed transaction
         */

        try {

            quote.setPrice(newPrice);
            quote.setVolume(quote.getVolume() + sharesTraded);
            quote.setChange((newPrice.subtract(quote.getOpen()).doubleValue()));

            entityManager.getTransaction().begin();
            entityManager.merge(quote);
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }

        this.publishQuotePriceChange(quote, oldPrice, changeFactor, sharesTraded);

        return quote;
    }

    public Collection<Holding> getHoldings(String userID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getHoldings", userID);
        EntityManager entityManager = emf.createEntityManager();
        /*
         * managed transaction
         */
        entityManager.getTransaction().begin();

        Query query = entityManager.createNamedQuery("holding.holdingsByUserID");
        query.setParameter("userID", userID);

        entityManager.getTransaction().commit();
        Collection<Holding> holdings = query.getResultList();
        /*
         * Inflate the lazy data memebers
         */
        Iterator itr = holdings.iterator();
        while (itr.hasNext()) {
            ((Holding) itr.next()).getQuote();
        }

        entityManager.close();
        entityManager = null;
        return holdings;
    }

    public Holding getHolding(Integer holdingID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getHolding", holdingID);
        Holding holding;
        EntityManager entityManager = emf.createEntityManager();
        holding = entityManager.find(Holding.class, holdingID);
        entityManager.close();
        return holding;
    }

    public  Account getAccountData(String userID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getAccountData", userID);

        EntityManager entityManager = emf.createEntityManager();

        AccountProfile profile = entityManager.find(AccountProfile.class, userID);
        /*
         * Inflate the lazy data memebers
         */
         Account account = profile.getAccount();
        account.getProfile();

        // Added to populate transient field for account
        account.setProfileID(profile.getUserID());
        entityManager.close();
        entityManager = null;

        return account;
    }

    public AccountProfile getAccountProfileData(String userID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:getProfileData", userID);
        EntityManager entityManager = emf.createEntityManager();

        AccountProfile apb = entityManager.find(AccountProfile.class, userID);
        entityManager.close();
        entityManager = null;
        return apb;
    }

    public AccountProfile updateAccountProfile(AccountProfile profileData) {

        EntityManager entityManager = emf.createEntityManager();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:updateAccountProfileData", profileData);
        /*
         * // Retrieve the previous account profile in order to get account
         * data... hook it into new object AccountProfile temp =
         * entityManager.find(AccountProfile.class,
         * profileData.getUserID()); // In order for the object to merge
         * correctly, the account has to be hooked into the temp object... // -
         * may need to reverse this and obtain the full object first
         * 
         * profileData.setAccount(temp.getAccount());
         * 
         * //TODO this might not be correct temp =
         * entityManager.merge(profileData); //System.out.println(temp);
         */

        AccountProfile temp = entityManager.find(AccountProfile.class, profileData.getUserID());
        temp.setAddress(profileData.getAddress());
        temp.setPassword(profileData.getPassword());
        temp.setFullName(profileData.getFullName());
        temp.setCreditCard(profileData.getCreditCard());
        temp.setEmail(profileData.getEmail());
        /*
         * Managed Transaction
         */
        try {

            entityManager.getTransaction().begin();
            entityManager.merge(temp);
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        return temp;
    }

    public  Account login(String userID, String password)
    throws Exception {

        EntityManager entityManager = emf.createEntityManager();

        AccountProfile profile = entityManager.find(AccountProfile.class, userID);

        if (profile == null) {
            throw new RuntimeException("No such user: " + userID);
        }
        /*
         * Managed Transaction
         */
        entityManager.getTransaction().begin();
        entityManager.merge(profile);

         Account account = profile.getAccount();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:login", userID, password);

        account.login(password);
        entityManager.getTransaction().commit();
        if (Log.doTrace())
            Log.trace("TradeJPADirect:login(" + userID + "," + password + ") success" + account);
        entityManager.close();
        return account;
    }

    public void logout(String userID) {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:logout", userID);
        EntityManager entityManager = emf.createEntityManager();

        AccountProfile profile = entityManager.find(AccountProfile.class, userID);
         Account account = profile.getAccount();

        /*
         * Managed Transaction
         */
        try {
            entityManager.getTransaction().begin();
            account.logout();
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        if (Log.doTrace())
            Log.trace("TradeJPADirect:logout(" + userID + ") success");
    }

    public  Account register(String userID, String password, String fullname, 
                                    String address, String email, String creditcard,
                                    BigDecimal openBalance) {
         Account account = null;
        AccountProfile profile = null;
        EntityManager entityManager = emf.createEntityManager();

        if (Log.doTrace())
            Log.trace("TradeJPADirect:register", userID, password, fullname, address, email, creditcard, openBalance);

        // Check to see if a profile with the desired userID already exists

        profile = entityManager.find(AccountProfile.class, userID);

        if (profile != null) {
            Log.error("Failed to register new Account - AccountProfile with userID(" + userID + ") already exists");
            return null;
        }
        else {
            profile = new AccountProfile(userID, password, fullname,
                                                 address, email, creditcard);
            account = new Account(0, 0, null, new Timestamp(System.currentTimeMillis()), openBalance, openBalance, userID);
            profile.setAccount(account);
            account.setProfile(profile);
            /*
             * managed Transaction
             */
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(profile);
                entityManager.persist(account);
                entityManager.getTransaction().commit();
            }
            catch (Exception e) {
                entityManager.getTransaction().rollback();
            } finally {
                entityManager.close();
            }

        }

        return account;
    }

    // @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:resetTrade", deleteAll);

        return(new TradeJDBCDirect(false)).resetTrade(deleteAll);
    }

    /*
     * NO LONGER USE
     */

    private void publishQuotePriceChange(Quote quote,
                                         BigDecimal oldPrice, BigDecimal changeFactor, double sharesTraded) {
        if (!TradeConfig.getPublishQuotePriceChange())
            return;
        Log.error("TradeJPADirect:publishQuotePriceChange - is not implemented for this runtime mode");
        throw new UnsupportedOperationException("TradeJPADirect:publishQuotePriceChange - is not implemented for this runtime mode");
    }

    /*
     * new Method() that takes EntityManager as a parameter
     */
    private Order createOrder( Account account,
                                      Quote quote, Holding holding, String orderType,
                                      double quantity, EntityManager entityManager) {
        Order order;
        if (Log.doTrace())
            Log.trace("TradeJPADirect:createOrder(orderID=" + " account="
                      + ((account == null) ? null : account.getAccountID())
                      + " quote=" + ((quote == null) ? null : quote.getSymbol())
                      + " orderType=" + orderType + " quantity=" + quantity);
        try {
            order = new Order(orderType, 
                                      "open", 
                                      new Timestamp(System.currentTimeMillis()), 
                                      null, 
                                      quantity, 
                                      quote.getPrice().setScale(FinancialUtils.SCALE, FinancialUtils.ROUND),
                                      TradeConfig.getOrderFee(orderType), 
                                      account, 
                                      quote, 
                                      holding);
                entityManager.persist(order);
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            Log.error("TradeJPADirect:createOrder -- failed to create Order", e);
            throw new RuntimeException("TradeJPADirect:createOrder -- failed to create Order", e);
        }
        return order;
    }

    /*private Holding createHolding( Account account,
                                          Quote quote, double quantity, BigDecimal purchasePrice,
                                          EntityManager entityManager) throws Exception {
        Holding newHolding = new Holding(quantity,
                                                         purchasePrice, new Timestamp(System.currentTimeMillis()),
                                                         account, quote);
        try {            
            entityManager.persist(newHolding);            
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
        } 
        
        return newHolding;
    }*/

    public double investmentReturn(double investment, double NetValue)
    throws Exception {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:investmentReturn");

        double diff = NetValue - investment;
        double ir = diff / investment;
        return ir;
    }

    public Quote pingTwoPhase(String symbol) throws Exception {
        Log
        .error("TradeJPADirect:pingTwoPhase - is not implemented for this runtime mode");
        throw new UnsupportedOperationException("TradeJPADirect:pingTwoPhase - is not implemented for this runtime mode");
    }

    class quotePriceComparator implements java.util.Comparator {
        public int compare(Object quote1, Object quote2) {
            double change1 = ((Quote) quote1).getChange();
            double change2 = ((Quote) quote2).getChange();
            return new Double(change2).compareTo(change1);
        }
    }

    /**
     * TradeBuildDB needs this abstracted method
     */
    public String checkDBProductName() throws Exception {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:checkDBProductName");
        return(new TradeJDBCDirect(false)).checkDBProductName();
    }

    /**
     * TradeBuildDB needs this abstracted method
     */
    public boolean recreateDBTables(Object[] sqlBuffer, java.io.PrintWriter out)
    throws Exception {
        if (Log.doTrace())
            Log.trace("TradeJPADirect:checkDBProductName");
        return(new TradeJDBCDirect(false)).recreateDBTables(sqlBuffer, out);
    }

    /**
     * Get mode - returns the persistence mode (TradeConfig.JPA)
     * 
     * @return int mode
     */
    public int getMode() {
        return TradeConfig.JPA;
    }

}
