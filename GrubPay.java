import java.util.*;
import java.text.*;
import java.io.*;

public class GrubPay{
  public static void main(String[] args){	  
    Scanner input = new Scanner(System.in);
    if(invalidInput(args))
      arguementsRules();

    String filePath = getFilePath(args);
    File file = new File(filePath);
    try{
      if(file.createNewFile()){
        setupFile(args, file);
      }
      String toPay = getPersonToPay(file);

      log("It is "+ toPay +"'s turn to pay"
          + "\nConfirm payment? (y/n)");
      String answer = input.next();
      if(answer.equals("y"))
        confirmPay(toPay, file);
      else if(answer.equals("n"))
        log("No payment is done."
            + "\nFile is not modified");
      else
        log("unrecognized answer.");
    }
    catch(Exception e) {
      log("Exception caught in main");
      log(e);
    }
    exitProgram();

  }

  private static Boolean invalidInput(String[] args){
    if(args.length > 1 && oneLetterEach(args))
      return Boolean.FALSE;
    else
      return Boolean.TRUE;
  }

  private static Boolean oneLetterEach(String[] args){
    for(String a : args){
      if(a.length() > 1)
        return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  private static void arguementsRules(){
    log("\nYour arguements must follow the correct format as follows:"
        + "\nTwo or more letters seperated by space representing person's name."
        + "\nA file with these initials must not already exist."
        + "\n\nExamples:"
        + "\nh m: Hadi & Mohammad."
        + "\nm m: Mohammad & Mostafa."
        + "\nm h: Mostafa & Hadi."
        + "\nh m m: Hadi, Mohammad & Mostafa.");
    exitProgram();
  }

  private static void log(Object aMsg){
    System.out.println(String.valueOf(aMsg));
  }

  private static void exitProgram(){
    log("\nExiting program...");
    System.exit(0);
  }

  private static String getFilePath(String[] args){
    String result = "";
    for(String a : args)
      result += a;
    return "./" + result + ".gh";
  }

  private static void setupFile(String[] args, File file){
    Scanner input = new Scanner(System.in);
    try{
      BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
      log("First time ordering, creating new file...");
      for(String s : args){
        log("Who does "+ s +" stands for?");
        String in = input.next();
        out.write(s + " : " + in + "\n");
      }
      out.close();
      log("file was setup");
    }
    catch (Exception e) {
      log("Exception caught in setupFile");
      log(e);
    }
  }

  private static String getPersonToPay(File file){
    Scanner scanner = null;
    try{
      scanner = new Scanner(file);
      ArrayList<String> names = getNames(file);
      String lastLine = getLastLine(file);
      if(hasName(lastLine)){
        log("This is a new file."
            + "\nGenerating a random person to pay...");
        Random r = new Random();
        return names.get(r.nextInt(names.size()));
      }
      else{
        String lastPayer = getLastPayer(lastLine);
        log("Last time, "+ lastPayer +" paid."
            + "\nGenerating next person to pay...");
        int i = (names.indexOf(lastPayer) + 1) % names.size();
        return names.get(i);
      }
    }
    catch(Exception e){
      log("Exception caught in getPersonToPay");
      log(e);
    }
    finally{
      scanner.close();
    }
    return null;
  }

  private static ArrayList<String> getNames(File file) {
    Scanner scanner = null;
    ArrayList<String> names = new ArrayList<String>();
    try{
      scanner = new Scanner(file);
      while(scanner.hasNextLine()){
        String line = scanner.nextLine();
        if(hasName(line))
          names.add(getNameFromLine(line));
      }
    }
    catch(Exception e){
      log("Exception caught in getNames");
      log(e);
    }
    finally{
      scanner.close();
    }
    return names;
  }

  private static Boolean hasName(String line) {
    Scanner scanner = new Scanner(line);
    scanner.useDelimiter(" : ");
    if(scanner.hasNext()){
      scanner.next(); // unneeded info
      return scanner.hasNext();
    }
    else
      return false;
  }
  private static String getNameFromLine(String line) {
    Scanner scanner = new Scanner(line);
    scanner.useDelimiter(" : ");
    scanner.next(); // unneeded info
    return scanner.next();
  }

  private static String getLastLine(File file){
    Scanner scanner = null;
    String line = "noLineFound";
    try{
      scanner = new Scanner(file);
      while(scanner.hasNextLine())
        line = scanner.nextLine();
    }
    catch(Exception e){
      log("Exception caught in getLastLine");
      log(e);
    }
    finally{
      scanner.close();
    }
    return line;
  }

  private static String getLastPayer(String line){
    Scanner scanner = new Scanner(line);
    scanner.useDelimiter(": ");
    scanner.next(); // unneeded info
    return scanner.next();
  }

  private static void confirmPay(String name, File file){
    log("Confirmed. "+ name +" is going to pay."
        + "\nModifying file...");
    try{
      BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath(),true));
      out.write(getDate() + ": " + name + "\n");
      out.close();
    }
    catch(Exception e) {
      log("Exception caught in confirmPay");
      log(e);
    }
    log("Writing in file succeeded.");
  }

  private static String getDate(){
    Date now = new Date();
    DateFormat df = DateFormat.getDateInstance();
    return df.format(now);
  }
}
