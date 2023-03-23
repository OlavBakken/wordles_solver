import java.util.regex.*;

public class Test {
    public static void main(String[] args){
        Pattern p = Pattern.compile("(\\d+).(\\d+).(\\d+).(\\d+).(\\d+)");
        String s = "0 0 1 3 5";
        Matcher matcher = p.matcher(s);
        matcher.find();
        for (int i = 0; i < 6; i++) {
            System.out.println(matcher.group(i));
        }
    }
}
