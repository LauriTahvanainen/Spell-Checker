package kvk.algoritmi;

import java.io.Serializable;

public interface IMuokkausEtaisyyslaskija extends Serializable {

    int laskeEtaisyys(String sana1, String sana2);
    
    @Override
    String toString();
}
