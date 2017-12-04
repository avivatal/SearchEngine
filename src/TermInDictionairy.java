public class TermInDictionairy {

    int idf;
    int pointerToPosting;

    public TermInDictionairy() {
        this.idf = 1;
        this.pointerToPosting = 0;
    }

    public int getIdf() {
        return idf;
    }

    public void setIdf() {
        this.idf++;
    }

    public int getPointerToPosting() {
        return pointerToPosting;
    }

    public void setPointerToPosting(int pointerToPosting) {
        this.pointerToPosting = pointerToPosting;
    }
}


