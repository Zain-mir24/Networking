import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ObjectOutputStream objectOutputStream;
    static ServerSocket myServerSocket;
    static Socket skt;
    static ArrayList<zoo> inputs=new ArrayList<zoo>();
    static ArrayList<zoo2> input=new ArrayList<zoo2>();
    static ArrayList<cage_reservation> reserve = new ArrayList<cage_reservation>();
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        retrieveData();
        retrievefood();
        retrieveReservation();
        myServerSocket = new ServerSocket(1342);
        while (true){
            skt = myServerSocket.accept();
            ObjectInputStream objectInput = new ObjectInputStream(skt.getInputStream());
            Object object = objectInput.readObject();
            System.out.println(object.getClass());
            if(object.getClass().equals(Integer.class)){
                int val=(int)object;
                System.out.println(val);
//                if(val==0){
//                    ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
//                    objectOutput.writeObject(inputs.size());
//                }
                if(val==1){
                    ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                    objectOutput.writeObject(inputs);
                }
                if(val==2 ){
                    ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                    objectOutput.writeObject(input);
                }
                if(val==3){
                    ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                    objectOutput.writeObject(reserve);
                }
            }
            else if((object.getClass().equals(String.class))) {
                String val = (String) object;
                System.out.println("STRING OBJECT");
                zoo found = new zoo("null", "Not Found", "", 0, "");
                for (zoo i : inputs) {
                    if (i.animal.equalsIgnoreCase(val)) {
                        found = i;
                    }
                }
                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                objectOutput.writeObject(found);
            }
            else if((object.getClass().equals(zoo2search.class))){
                zoo2search val=(zoo2search) object;
                String value=val.data;
                System.out.println("Zoo2 Object");
                zoo2 found=new zoo2("Not Found","",0,"");
                for(zoo2 i : input){
                    if(i.cage.equalsIgnoreCase(value)){
                        found=i;
                    }
                }
                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                objectOutput.writeObject(found);
            }
            else if((object.getClass().equals(reserve_search.class))){
                reserve_search val=(reserve_search) object;
                String value=val.data;
                System.out.println("Reserve_Send Object");
                cage_reservation found=new cage_reservation("Not Found");
                for(cage_reservation i : reserve){
                    if(i.getID().equalsIgnoreCase(value)){
                        found=i;
                    }
                }
                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                objectOutput.writeObject(found);
            }
            else if(object.getClass().equals(ArrayList.class)){
                ArrayList<zoo> newData =  (ArrayList<zoo>) object;
                mergeArray(newData);
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("animal.txt")));
                objectOutputStream.writeObject(inputs);
                objectOutputStream.flush();
                objectOutputStream.close();
                System.out.println("Data Successfully Saved to Database");
            }
            else if(object.getClass().equals(zoo2send.class)){
                zoo2send recieved = (zoo2send)object;
                ArrayList<zoo2> newData =  (ArrayList<zoo2>) recieved.data;
//                String val = newData.get(0).animal.getAnimal();
//
//                for(zoo i : inputs){
//                    if(i.animal.equalsIgnoreCase(val)){
//                        newData.get(0).animal=i;
//                        i.setCage(newData.get(0));
//                    }
//                }
                mergeArray1(newData);
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("food.txt")));
                objectOutputStream.writeObject(input);
                objectOutputStream.flush();
                objectOutputStream.close();
                System.out.println("Data Successfully Saved to Database");
            }
            else if(object.getClass().equals(reserve_send.class)) {
                reserve_send recieved = (reserve_send) object;
                ArrayList<cage_reservation> newData = (ArrayList<cage_reservation>) recieved.data;
                String AnimalID = newData.get(0).getAnimal().getID();
                System.out.println(AnimalID);
                String CageID = newData.get(0).getCage().getID();
                System.out.println(CageID);
                zoo foundAnimal = new zoo();
                zoo2 foundCage = new zoo2();
                for (zoo i : inputs) {
                    if (i.getID().equalsIgnoreCase(AnimalID)) {
//                        newData.get(0).setAnimal(i);
                        foundAnimal=i;
                    }
                }
                if (foundAnimal.getAnimal().equalsIgnoreCase("null")) {
                    newData.get(0).setMsg("Animal Not Found");
                    System.out.println("Animal Not Found");
                    ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                    objectOutput.writeObject(newData);
                }
                else {
                    for (zoo2 i : input) {
                        if (i.getID().equalsIgnoreCase(CageID)) {
//                        newData.get(0).setCage(i);
                            foundCage = i;
                        }
                    }
                    if (foundCage.getCageName().equalsIgnoreCase("null")) {
                        newData.get(0).setMsg("Cage Not Found");
                        System.out.println("Cage Not Found");
                        ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                        objectOutput.writeObject(newData);
                    } else {
                        if (foundAnimal.getType().equalsIgnoreCase(foundCage.getType())) {
                            if(foundAnimal.getQuantity()<=foundCage.getQuantity()){
                                newData.get(0).setAnimal(foundAnimal);
                                newData.get(0).setCage(foundCage);
                                newData.get(0).setMsg("Success");
                                mergeArray2(newData);
                                objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("reserve.txt")));
                                objectOutputStream.writeObject(newData);
                                objectOutputStream.flush();
                                objectOutputStream.close();
                                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                                objectOutput.writeObject(newData);
                            }
                            else {
                                newData.get(0).setMsg("Cage Size is smaller than Animal Quantity");
                                System.out.println("Size Not Matched");
                                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                                objectOutput.writeObject(newData);
                            }
                        }
                        else {
                            newData.get(0).setMsg("Type Not Matched");
                            System.out.println("Cage Not Matched");
                            ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
                            objectOutput.writeObject(newData);
                        }
                    }
                }
            }
            else{
                System.out.println(object.getClass());
            }
        }
    }

    private static void mergeArray(ArrayList<zoo> newData) {
        for(zoo i:newData){
            inputs.add(i);
        }
    }
    private static void mergeArray1(ArrayList<zoo2> newData) {
        for(zoo2 i:newData){
            input.add(i);
        }
    }
    private static void mergeArray2(ArrayList<cage_reservation> newData) {
        for(cage_reservation i:newData){
            reserve.add(i);
        }
    }



    private static void retrieveData() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("animal.txt")));
            Object obj=inputStream.readObject();
            if(obj!=null){
                inputs = (ArrayList<zoo>) obj;}
        }
        catch (Exception e){

        }
    }
    private static void retrievefood() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("food.txt")));
            Object obj=inputStream.readObject();
            if(obj!=null){
                input = (ArrayList<zoo2>) obj;}
        }
        catch (Exception e){

        }
    }
    private static void retrieveReservation() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("reserve.txt")));
            Object obj=inputStream.readObject();
            if(obj!=null){
                reserve = (ArrayList<cage_reservation>) obj;}
        }
        catch (Exception e){

        }
    }
}