package analyzer;

public class PatternType implements Comparable<PatternType> {
    private int priority;
    private String pattern;
    private String printType;

    public PatternType(int priority, String pattern, String printType) {
        this.priority = priority;
        this.pattern = pattern;
        this.printType = printType;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getPrintType() {
        return printType;
    }

    public void printLine() {
        System.out.println(getPriority() + " " + getPattern() + " " + getPrintType());
    }

    @Override
    public int compareTo(PatternType o) {
        return Integer.valueOf(o.getPriority()).compareTo(getPriority());
    }
}
