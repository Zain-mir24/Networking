import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static Scanner x = new Scanner(System.in);
    static ArrayList<zoo> inputs = new ArrayList<zoo>();
    static ArrayList<zoo2> inputs1 = new ArrayList<zoo2>();
    static ArrayList<cage_reservation> reservation = new ArrayList<cage_reservation>();
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println("Welcome to Zain & Ahmed Zoo Management System");
        while (true) {
            System.out.println("Enter your Choice");
            System.out.println("1) Animal Management");
            System.out.println("2) Create Cage");
            System.out.println("3) Cage Reservation");
            System.out.println("4) Exit");
            int val=x.nextInt();
            if(val==1){
                while (true){
                    System.out.println("Enter your Choice");
                    System.out.println("1) Add Animal");
                    System.out.println("2) View Animal");
                    System.out.println("3) Search Animal");
                    System.out.println("4) Exit");
                    int answer=x.nextInt();
                    if(answer==1){
                        SendData();
                    }
                    else if(answer==2){
                        RequestView();
                    }
                    else if(answer==3){
                        searchFor();
                    }
                    else if(answer==4){
                        break;
                    }

                }
            }
            else if(val==2){
                while (true){
                    System.out.println("Enter your Choice");
                    System.out.println("1) Addcage");
                    System.out.println("2) view cage");
                    System.out.println("3) search cage");
                    System.out.println("4) break");
                    int answer=x.nextInt();
                    if(answer==1){
                        SendCage();
                    }
                    else if(answer==2){
                        RequestCage();
                    }
                    else if(answer==3){
                        searchForFood();
                    }
                    else if(answer==4){
                        break;
//                            searchForFood();
                    }


                }
            }
            else if(val==3){
                System.out.println("Enter your Choice");
                System.out.println("1) Assign Cage");
                System.out.println("2) view Data");
                System.out.println("3) Search Reservation");
                System.out.println("4) exit data");
//                System.out.println("3) search Food");
                int answer=x.nextInt();
                if(answer==1){
                    Cage_Reservation();
                }
                else if(answer==2){
                    RequestReservation();
                }
                else if(answer==3){
                    searchForReservation();
                }

            }
            else if(val==4){
                break;
            }

        }


    }

    private static void searchFor() throws IOException, ClassNotFoundException {
        System.out.println("Enter name of animal to search for");
        String toSearch=x.next();
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(toSearch);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        zoo results=(zoo) object;
        if(results.animal.equalsIgnoreCase("Not Found")){
            System.out.println("No Results Found");
        }
        else{
            System.out.println("Result Found");
            results.display();
        }
    }

    //    Searching for food
    private static void searchForFood() throws IOException, ClassNotFoundException {
        System.out.println("Enter food to search for");
        String toSearch=x.next();
        zoo2search item=new zoo2search(toSearch);
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(item);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        zoo2 results=(zoo2) object;
        if(results.cage.equalsIgnoreCase("Not Found")){
            System.out.println("No Results Found");
        }
        else{
            System.out.println("Result Found");
            results.display();
        }
    }

    public static void searchForReservation() throws IOException, ClassNotFoundException{
        System.out.println("Enter Reservation ID to search for");
        String toSearch=x.next();
        reserve_search item=new reserve_search(toSearch);
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(item);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        cage_reservation results=(cage_reservation) object;
        if(results.getID().equalsIgnoreCase("Not Found")){
            System.out.println("No Results Found");
        }
        else{
            System.out.println("Result Found");
            results.display();
        }
    }


    private static void RequestView() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(1);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        ArrayList<zoo> results=(ArrayList<zoo>) object;
        for(zoo i : results){
            i.display();
        }
    }

//    viewing food

    private static void RequestCage() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(2);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        ArrayList<zoo2> results=(ArrayList<zoo2>) object;
        for(zoo2 i : results){
            i.display();
        }
    }

    private static void RequestReservation() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1342);
        ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectOutput.writeObject(3);

        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        Object object = objectInput.readObject();
        ArrayList<cage_reservation> results=(ArrayList<cage_reservation>) object;
        for(cage_reservation i : results){
            i.display();
        }
    }

    public static void SendData(){

        inputs.clear();
        System.out.println("Enter ID:");
        String id = x.next();
        System.out.println("Enter animal");
        String animal= x.next();
        System.out.println("Enter Quantity");
        int quaan = x.nextInt();
        System.out.println("Enter Type");
        String type = x.next();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date=new Date();
        zoo item = new zoo(id,animal, type, quaan, df.format(date));
        inputs.add(item);
        System.out.println("Uploading");
        try {
            Socket socket = new Socket("127.0.0.1", 1342);
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(inputs);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e);
        }
    }


    public static void SendCage(){
        inputs1.clear();


        System.out.println("Enter id");
        String id = x.next();
        System.out.println("Enter Cage Name");
        String Food= x.next();
        System.out.println("Enter Quantity");
        int quantity = x.nextInt();
        System.out.println("Enter Type");
        String type = x.next();


        zoo2 item = new zoo2(Food, id, quantity,type);
        inputs1.add(item);
        zoo2send itemm= new zoo2send(inputs1);
        System.out.println("Uploading");
        try {
            Socket socket = new Socket("127.0.0.1", 1342);
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(itemm);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e);
        }

    }

    public static void Cage_Reservation() throws IOException, ClassNotFoundException{
        reservation.clear();
        System.out.println("Reservation ID:");
        String id = x.next();
        System.out.println("Enter Animal ID:");
        String AnimalId = x.next();
        System.out.println("Enter Cage ID:");
        String CageId = x.next();

        zoo Animal = new zoo(AnimalId);
        zoo2 Cage = new zoo2(CageId);
        cage_reservation reserve = new cage_reservation(Animal,Cage,id);
        reservation.add(reserve);
        reserve_send item = new reserve_send(reservation);
        try {
            Socket socket = new Socket("127.0.0.1", 1342);
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(item);
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            Object object = objectInput.readObject();
            ArrayList<cage_reservation> results=(ArrayList<cage_reservation>) object;
            System.out.println(results.get(0).getMsg());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e);
        }
//        Socket socket = new Socket("127.0.0.1", 1342);
//        ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
//        Object object = objectInput.readObject();
//        ArrayList<cage_reservation> results=(ArrayList<cage_reservation>) object;
//        System.out.println(results.get(0).getMsg());
    }


}

//class1
class zoo implements Serializable {
    String animal;
    String type;
    int quantity;
    String date;
    String ID;

    zoo(String id,String Animal, String Type, int quan, String dat) {
        ID=id;
        animal=Animal;
        type = Type;
        quantity = quan;
        date = dat;
    };

    public zoo(String id){
        ID=id;
        animal="null";
    }
    public zoo(){
        animal="null";
    }


//    public void setCage(zoo2 Cage){
//        this.cage=Cage;
//    }

//    public zoo2 getCage(){
//        return cage;
//    }

    public void display(){
        System.out.println(animal+"---"+type+"---"+quantity+"---"+ID);
    }

    public String getDate() {
        return date;
    }

    public String getID() {
        return ID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String Animal) {
        this.animal = animal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
// class 2
class zoo2 implements Serializable{
    String cage, id, type;
    zoo animal;
    int quantity;

    public zoo2(String cage, String id, int quantity, String type) {
        this.cage = cage;
        this.id = id;
        this.quantity = quantity;
        this.type = type;
    }
    public zoo2(String cage, String id, int quantity, String type, zoo Animal) {
        this.cage = cage;
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.animal= Animal;
    }
    public zoo2(String id){
        this.id=id;
        cage="null";
    }

    public zoo2() {
        cage="null";
        id="Not Allocated";
        quantity=0;
        type="Not Allocated";
    }

    public String getID(){
        return id;
    }

    public String getCageName(){
        return cage;
    }
    public void setAnimal(zoo Animal){
        this.animal=Animal;
    }

    public int getQuantity(){
        return quantity;
    }
    public String getType(){
        return type;
    }

    public void display(){
//        System.out.println(cage);
//        System.out.println(id);
//        System.out.println(quantity);
//        System.out.println(type);

        System.out.println("'ID:' "+id+" Cage: "+cage+" 'Animal Quantity:' "+quantity+" 'Type of Cage:' "+type);
    }

}

class zoo2send implements Serializable{
    ArrayList<zoo2> data;

    public zoo2send(ArrayList<zoo2> data) {
        this.data = data;
    }
}

class zoo2search implements Serializable{
    String data;

    public zoo2search(String data) {
        this.data = data;
    }
}

class cage_reservation implements  Serializable{
    zoo Animal;
    zoo2 Cage;
    String msg;
    String id;

    public cage_reservation(zoo animal, zoo2 cage,String id){
        Animal=animal;
        Cage=cage;
        this.id=id;
    }
    public cage_reservation(String id){
        this.id=id;
    }

    public void setAnimal(zoo animal){
        Animal=animal;
    }
    public void setCage(zoo2 cage){
        Cage=cage;
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
    public String getMsg(){
        return msg;
    }
    public String getID(){
        return id;
    }

    public void display(){
        System.out.println("Animal: "+Animal.getAnimal()+"---Cage: "+Cage.getCageName());
    }
    public zoo getAnimal(){
        return Animal;
    }

    public zoo2 getCage(){
        return Cage;
    }

}

class reserve_send implements Serializable{
    ArrayList<cage_reservation> data;
    public reserve_send(ArrayList<cage_reservation> data) {
        this.data = data;
    }
}

class reserve_search implements Serializable{
    String data;

    public reserve_search(String  data) {
        this.data = data;
    }
}