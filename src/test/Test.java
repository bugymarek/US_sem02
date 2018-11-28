package test;

import dynamicHashingCore.DynamicHashing;
import dynamicHashingCore.IRecord;
import java.util.ArrayList;
import java.util.Random;

public class Test {

    private DynamicHashing dynamicHashing;
    private final int RANGE_ISERT = 9999999;

    Test(DynamicHashing dynamicHashing) {
        this.dynamicHashing = dynamicHashing;
    }

    public boolean checkInsert() {
        Random randomGenerator = new Random(2);
        ArrayList<Realty> arr = new ArrayList<>();
        ArrayList<Integer> arrID = new ArrayList<>();
        int duplicite = 0;
        for (int j = 0; j < 3000; j++) {
            int Id = randomGenerator.nextInt(RANGE_ISERT);
            Realty realty = new Realty(Id, 1516541, "janko" + Id, "mrkvicka" + Id);
            System.out.println(j+ ". " + "Vkladam id: " + realty.getId());
            if (!arrID.contains(realty.getId())) {
                arrID.add(realty.getId());
                arr.add(realty);
                boolean result = dynamicHashing.add(realty);
                dynamicHashing.find(new test.Realty(5413533));
                if (!result) {
                    duplicite++;
                    System.out.println("Prvok s rovnakym klucom sa uz v strome nachadza");
                }
            }
        }
        
        System.out.println("****************************************************** pocet duplicit: " + duplicite + "*********************************************");


        for (int k = 0; k < arr.size(); k++) {
            Realty realtyArr = arr.get(k);
            IRecord realtyDH = dynamicHashing.find(realtyArr);
            System.out.println("*************************************************************************** find");
            if (realtyDH != null) {
                //System.out.println("true: DataArr: (" + personArr.getRC() + ")   " + "DataTree: (" + personTree.toString() + ")");
                //System.out.println("Seed: " + i);
            } else {
                System.out.println("*************************************************************************** find");
                System.out.println("false, prvok sa v strome nenasiel: " + realtyArr.toString());
                return false;
            }
        }
        return true;
    }
}
