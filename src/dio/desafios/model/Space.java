package dio.desafios.model;

public class Space {

    private Integer actual;
    private final int expeceted;
    private final boolean fixed;


    public Space(int expeceted, boolean fixed) {
        this.expeceted = expeceted;
        this.fixed = fixed;
        if(fixed){
            actual = expeceted;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public void clearSpace(){
        setActual(null);
    }

    public int getExpeceted() {
        return expeceted;
    }

    public boolean isFixed() {
        return fixed;
    }
}
