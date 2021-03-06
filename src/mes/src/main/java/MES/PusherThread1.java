package MES;

public class PusherThread1 implements Runnable {
    @Override
    public void run() {

        System.out.println("--------------[Executing] PusherThread1 is Running [Executing]--------------");

        int full1=1;
        int full2=1;
        int full3=1;
        int orderID=0;
        int unitType=0;
        int orderKey;
        String stringOrderKey;

        Pusher p1= (Pusher) SFS.getCell(3, 7);
        Pusher p2= (Pusher) SFS.getCell(4, 7);
        Pusher p3= (Pusher) SFS.getCell(5, 7);

        while(true){

            if(p1.isPushing() && full1==1){
                //Get orderID receiving
                orderKey = p1.getOrderPushing();
                stringOrderKey = String.valueOf(orderKey);

                //Get unit type to upload stats
                unitType = p1.getUnitTypePushing();
                p1.addUnloadStats(unitType);
                p1.addUnitCount();

                updateOrder(orderKey);

                try {
                    orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in unload lists
                }
                catch (Exception e) {
                }

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                for (int i=0; i < Main.orderListUnloadEnded.size(); i++){
                    if((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListUnloadEnded.get(i).getQuantity())){
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                        Main.orderListUnloadEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                        Main.orderListUnloadEnded.get(i).setStatus(4);
                        System.out.println("Order Unload fim no Pusher 1 -> " + Main.orderListUnloadEnded.get(i));
                    }
                    else if ((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListUnloadEnded.get(i).getQuantity())) {
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                    }
                }

                full1=2;
            }
            if(!p1.isPushing() && full1==2){
                full1=1;
            }


            if(p2.isPushing() && full2==1){
                //Get orderID receiving
                orderKey = p2.getOrderPushing();
                stringOrderKey = String.valueOf(orderKey);

                //Get unit type to upload stats
                unitType = p2.getUnitTypePushing();
                p2.addUnloadStats(unitType);
                p2.addUnitCount();

                updateOrder(orderKey);

                try {
                    orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in unload lists
                }
                catch (Exception e){
                }

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                for (int i=0; i < Main.orderListUnloadEnded.size(); i++){
                    if((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListUnloadEnded.get(i).getQuantity())){
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                        Main.orderListUnloadEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                        Main.orderListUnloadEnded.get(i).setStatus(4);
                        System.out.println("Order Unload fim no Pusher 2 -> " + Main.orderListUnloadEnded.get(i));
                    }
                    else if ((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListUnloadEnded.get(i).getQuantity())) {
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                    }
                }

                full2=2;
            }
            if(!p2.isPushing() && full2==2){
                full2=1;
            }


            if(p3.isPushing() && full3==1){
                //Get orderID receiving
                orderKey = p3.getOrderPushing();
                stringOrderKey = String.valueOf(orderKey);

                //Get unit type to upload stats
                unitType = p3.getUnitTypePushing();
                p3.addUnloadStats(unitType);
                p3.addUnitCount();

                updateOrder(orderKey);
                try {
                    orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in unload lists
                }
                catch(Exception e){
                }

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                for (int i=0; i < Main.orderListUnloadEnded.size(); i++){
                    if((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListUnloadEnded.get(i).getQuantity())){
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                        Main.orderListUnloadEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                        Main.orderListUnloadEnded.get(i).setStatus(4);
                        System.out.println("Order Unload fim no Pusher 3 -> " + Main.orderListUnloadEnded.get(i));
                    }
                    else if ((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListUnloadEnded.get(i).getQuantity())) {
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                    }
                }

                full3=2;
            }
            if(!p3.isPushing() && full3==2){
                full3=1;
            }

        }


    }

    private void updateOrder(int orderKey) {
        // If present in Hash Table updates otherwise creates new key with value 1
        //System.out.println(Main.receivedOrderPieces.containsKey(orderID));
        if(Main.receivedOrderPieces.containsKey(orderKey)){
            Main.receivedOrderPieces.computeIfPresent(orderKey, (k, v) -> v + 1);
        }
        else {
            Main.receivedOrderPieces.put(orderKey,1);
        }
    }
}

