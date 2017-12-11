import java.util.HashSet;

public class TermInDictionairy {

    int idf;
    int pointerToPosting;
    HashSet<TermInDoc> pointerToTermInCache;

//if there is a cache record for the term, the pointer to posting will remain zero, and the pointer to cache will hold a reference to the cache record.
// from the cache record pointed at, the posting record on disc is reachable.
//if there is no cache record for the term, the cache record will be null and the pointer to posting will be >0

    public TermInDictionairy() {
        this.idf = 1;
        this.pointerToPosting = 0;
        pointerToTermInCache =null;

    }

    public HashSet<TermInDoc> getPointerToTermInCache() {
        return pointerToTermInCache;
    }

    public void setPointerToTermInCache(HashSet<TermInDoc> pointerToTermInCache) {
        this.pointerToTermInCache = pointerToTermInCache;
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


