package test;

import com.easycode.framework.web.domain.server.Sys;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

public class Test {
    public void test(){
        int [] data ={45,54,1,12,11,2,1,5,6,45};
        HashSet<Object> setCollection = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            setCollection.add(data[i]);
        }
        Iterator<Object> iterator = setCollection.iterator();
        while((iterator.hasNext())){
            System.out.println(iterator.next());
        }
    }

    public static void main(String[] args) {
        function1();
//        function2();
    }

    private static void function2() {
        System.out.println("------------------方式二 ： 何时能花完钱---------------------------------");

        BigDecimal balance = new BigDecimal("121.73");
        BigDecimal balanceForfree = new BigDecimal("18.27");
        BigDecimal hardPrice = new BigDecimal("26");
        BigDecimal discount= new BigDecimal("0.14");
        BigDecimal discount2= new BigDecimal("0.86");
        BigDecimal balanceSum = BigDecimal.ZERO;
        int count = 0 ;
        do {
            count ++;
            balance = balance.subtract(hardPrice.multiply(discount2));
            balanceForfree = balanceForfree.subtract(hardPrice.multiply(discount));
            balanceSum = balance.add(balanceForfree);
        }while (balanceSum.compareTo(BigDecimal.ZERO) > 0);
        System.out.println("余额 ： " + balanceSum );
        System.out.println( "总计花费： " + hardPrice.multiply(new BigDecimal(count)).toString() + "总计消费：" + count);
    }

    private static void function1() {
        System.out.println("------------------总金额 140  ----------------");
        BigDecimal balance = new BigDecimal("121.73");
        BigDecimal discount= new BigDecimal("0.14");
        BigDecimal hardPrice = new BigDecimal("26");
        BigDecimal balanceForfree = new BigDecimal("18.27");
        BigDecimal sumKkbalance = BigDecimal.ZERO;
        BigDecimal actualCoumuse = BigDecimal.ZERO;
        int count = 0 ;
        do{
            count ++ ;
            if (balance.compareTo(BigDecimal.ZERO) < 0 ){
                System.out.println("------------------余额已经为 0 请充值---------------------------------");
                actualCoumuse = hardPrice.multiply(BigDecimal.valueOf(count));
                System.out.println("在稻南国消费的次数 + " + count
                        +  " 个人余额 ： " + balance.toString()
                        + "  赠送的余额" + balanceForfree.subtract(sumKkbalance).toString()
                        + " 实际花销" + actualCoumuse.toString());
                return;
            }
            balance = balance.subtract(hardPrice);
            BigDecimal multiply = hardPrice.multiply(discount);
            sumKkbalance = sumKkbalance.add(multiply);
        }
        while (sumKkbalance.compareTo(balanceForfree) < 0 );

        actualCoumuse = hardPrice.multiply(BigDecimal.valueOf(count));
        System.out.println("在稻南国消费的次数 + " + count
                +  " 个人余额 ： " + balance.toString()
                + "  赠送的余额" + balanceForfree.subtract(sumKkbalance).toString()
                + " 实际花销" + actualCoumuse.toString());
    }


}
