import java.util.Scanner;

public class Game {
    private static final double STARTING_BALANCE = 1000.0;
    private static final double STARTING_ASSET_PRICE = 100.0;
    private static final long GAME_DURATION_MS = 150000;
    private static final int STOCK_CHOICE = 1;
    private static final int CRYPTO_CHOICE = 2;
    private static final int COMMODITY_CHOICE = 3;

    AssetFactory assetFactory = new AssetFactory();

    Player player = new Player(STARTING_BALANCE);
    Asset assetStock = assetFactory.createAsset("STOCK", STARTING_ASSET_PRICE, "NVIDIA");
    Asset assetCrypto = assetFactory.createAsset("CRYPTO", STARTING_ASSET_PRICE, "Coinsy");
    Asset assetCommodity = assetFactory.createAsset("COMMODITY", STARTING_ASSET_PRICE, "Gold");
    Market marketStock = new Market(assetStock);
    Market marketCrypto = new Market(assetCrypto);
    Market marketCommodity = new Market(assetCommodity);

    //Teksto spalvos
    public static final String RESET = "\033[0m";
    public static final String BLUE = "\033[0;34m";
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String WHITE_BACKGROUND = "\033[47m";
    public static final String BLACK = "\033[0;30m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";


    public void play() {
        long startTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);

        while (System.currentTimeMillis() - startTime < GAME_DURATION_MS) {
            long remainingTime = GAME_DURATION_MS - (System.currentTimeMillis() - startTime);
            displayGameState(remainingTime);
            displaySMAIndicator();
            processUserAction(scanner);
        }

        scanner.close();
        gameOver();
    }

    private void displayGameState(long remainingTime) {
        System.out.println((BLUE + "Likes laikas: " + remainingTime / 1000) + "s" + RESET);
        System.out.println(BLACK + CYAN_BACKGROUND + "Asset: " + BLACK_BOLD + assetStock.getName() + " vertė yra " + BLACK_BOLD + String.format("%.1f", assetStock.getPrice()) + "€ " + RESET);
        System.out.println(BLACK + CYAN_BACKGROUND + "Asset: " + BLACK_BOLD + assetCrypto.getName() + " vertė yra " + BLACK_BOLD + String.format("%.1f", assetCrypto.getPrice()) + "€ " + RESET);
        System.out.println(BLACK + CYAN_BACKGROUND + "Asset: " + BLACK_BOLD + assetCommodity.getName() + " vertė yra " + BLACK_BOLD + String.format("%.1f", assetCommodity.getPrice()) + "€ " + RESET);
        System.out.println(BLACK + WHITE_BACKGROUND +"Jūsų balansas " + BLACK_BOLD + String.format("%.1f",player.getBalance()) + "€ " + RESET);
        System.out.println(BLACK + WHITE_BACKGROUND +"Jūsų NVIDIA kiekis " + BLACK_BOLD + player.getStockAmount() + " " + RESET);
        System.out.println(BLACK + WHITE_BACKGROUND +"Jūsų Coinsy kiekis " + BLACK_BOLD + player.getCryptoAmount() + " " + RESET);
        System.out.println(BLACK + WHITE_BACKGROUND +"Jūsų Gold kiekis " + BLACK_BOLD + player.getCommodityAmount() + " " + RESET);
    }

    private void displaySMAIndicator() {
        if(assetCrypto.queueIsReady()) {
            if(assetCrypto.getPrice() >= assetCrypto.queueAverage()){
                System.out.println(GREEN_BOLD + "SMA: " + String.format("%.1f",assetCrypto.queueAverage()) + " BETTER BUY Coinsy" + RESET);
            } else {
                System.out.println(RED_BOLD + "SMA: " + String.format("%.1f",assetCrypto.queueAverage()) + " BETTER SELL Coinsy" + RESET);
            }
        }
    }

    private void processUserAction(Scanner scanner) {
        System.out.println(BLUE + "1 = BUY | 2 = SELL | ENTER = HOLD" + RESET);
        String action = scanner.nextLine();

        if (action.equals("1")) {
            handleBuyAction(scanner);
        } else if (action.equals("2")) {
            handleSellAction(scanner);
        } else if (action.isEmpty()) {
            updateAllMarkets();
        } else {
            System.out.println(RED_BOLD + "Invalid input");
        }
    }

    private void handleBuyAction(Scanner scanner) {
        System.out.print("Kurį asset pirkti? 1=NVIDIA 2=Coinsy 3=Gold: ");
        int assetChoice = scanner.nextInt();
        System.out.print("Kiek norite nusipirkti?");
        int amount = scanner.nextInt();
        scanner.nextLine();

        Asset selectedAsset = getAssetByChoice(assetChoice);
        if(selectedAsset != null && !player.buy(selectedAsset, amount)) {
            System.out.println(RED_BOLD + "Neturi tiek pinigų");
        }
        updateAllMarkets();
    }

    private void handleSellAction(Scanner scanner) {
        System.out.print("Kurį asset parduoti? 1=NVIDIA 2=Coinsy 3=Gold: ");
        int assetChoice = scanner.nextInt();
        System.out.print("Kiek norite parduoti?");
        int amount = scanner.nextInt();
        scanner.nextLine();

        Asset selectedAsset = getAssetByChoice(assetChoice);
        if(selectedAsset != null && !player.sell(selectedAsset, amount)) {
            System.out.println(RED_BOLD + "Neturi tiek coins");
        }
        updateAllMarkets();
    }

    private Asset getAssetByChoice(int choice) {
        if(choice == STOCK_CHOICE) {
            return assetStock;
        } else if(choice == CRYPTO_CHOICE) {
            return assetCrypto;
        } else if(choice == COMMODITY_CHOICE) {
            return assetCommodity;
        }
        return null;
    }

    public void gameOver() {
        player.sell(assetStock, player.getStockAmount());
        player.sell(assetCrypto, player.getCryptoAmount());
        player.sell(assetCommodity, player.getCommodityAmount());

        System.out.println("Laikas baigėsi!");

        if(player.getBalance() > STARTING_BALANCE){
            System.out.println(GREEN_BOLD + "Sveikinu užsidirbus " + String.format("%.1f",(player.getBalance()) - STARTING_BALANCE) + "€" + RESET);
        }
        else if(player.getBalance() < STARTING_BALANCE){
            System.out.println(RED_BOLD + "Nepasiseke :( . Praradote " + String.format("%.1f",(STARTING_BALANCE -  player.getBalance())) + "€" + RESET);
        }
        else {
            System.out.println("Išlipote sausas");
        }
    }

    private void updateAllMarkets() {
        marketStock.updatePrice();
        marketCrypto.updatePrice();
        marketCommodity.updatePrice();
    }
}

