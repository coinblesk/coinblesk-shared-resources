package ch.uzh.csg.coinblesk.responseobject;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ServerSignatureRequestTransferObject extends TransferObject {
    
    
    private String partialTx; // Base64 encoded partial transaction
    private List<IndexAndDerivationPath> indexAndDerivationPaths;
    
    
    public String getPartialTx() {
        return partialTx;
    }

    public void setPartialTx(String partialTx) {
        this.partialTx = partialTx;
    }

    public List<IndexAndDerivationPath> getIndexAndDerivationPaths() {
        return indexAndDerivationPaths;
    }


    public void setIndexAndDerivationPaths(List<IndexAndDerivationPath> indexAndDerivationPaths) {
        this.indexAndDerivationPaths = indexAndDerivationPaths;
    }
    
    public void addIndexAndDerivationPath(int index, int[] path) {
        if(indexAndDerivationPaths == null) {
            indexAndDerivationPaths = new ArrayList<>();
        }
        this.indexAndDerivationPaths.add(new IndexAndDerivationPath(index, path));
    }


    /**
     * Clears the data stored in this transfer object
     */
    public void clear() {
        this.partialTx = null;
        this.indexAndDerivationPaths = new ArrayList<>();
    }

}
