package thread.sync.mall;

public class Mall {
    public synchronized void sale(){
        if ( Stock.count > 0 ) {
            try{
                Thread.sleep(5);
            }
            catch (InterruptedException e ){
                e.printStackTrace();
            }
            Stock.count -= 1 ;
            System.out.println("商品销售成功！");
        }
        else{
            System.out.println("商品库存不足，请下次再来！");
        }
    }

    public static void main(String[] args) {
        Mall mall = new Mall();
        for (int i = 0; i < 5 ; i++) {
            Consumer consumer = new Consumer() ;
            consumer.mall = mall ;
            Thread thread = new Thread(consumer) ;
            thread.start();
        }
    }
}
