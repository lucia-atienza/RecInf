public final class Pair<T, U> {

    public Pair(T first, U second) {
        this.second = second;
        this.first = first;
    }

    public T first;
    public U second;

    public static <T, U> Pair<T, U> pair(T first, U second) {
        return new Pair<>(first, second);
    }
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}