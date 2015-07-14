package ch.uzh.csg.coinblesk.responseobject;

/**
 * Wrapper class for index of input and derivation path used to sign a
 * transaction
 * 
 * @author rvoellmy
 *
 */
public class IndexAndDerivationPath {

    private final int index;
    private final int[] derivationPath;

    public IndexAndDerivationPath(int index, int[] derivationPath) {
        this.index = index;
        this.derivationPath = derivationPath;
    }

    public int getIndex() {
        return index;
    }

    public int[] getDerivationPath() {
        return derivationPath;
    }

}