package ie.teamchile.smartapp.activities.ServiceUserSearch;

import java.util.List;

import ie.teamchile.smartapp.model.AntiDHistory;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.VitKHistory;
import io.realm.Realm;

/**
 * Created by elliot on 27/12/2015.
 */
public class ServiceUserSearchModelImp implements ServiceUserSearchModel {
    private Realm realm;

    public ServiceUserSearchModelImp(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void saveVitKToRealm(List<VitKHistory> vitKHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(vitKHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveHearingToRealm(List<HearingHistory> hearingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(hearingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveNbstToRealm(List<NbstHistory> nbstHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(nbstHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveFeedingHistoriesToRealm(List<FeedingHistory> feedingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(feedingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserToRealm(BaseResponseModel baseResponseModel) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(baseResponseModel.getServiceUsers());
        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancies());
        realm.copyToRealmOrUpdate(baseResponseModel.getBabies());
        realm.copyToRealmOrUpdate(baseResponseModel.getAntiDHistories());
        realm.commitTransaction();
    }

    @Override
    public void deleteDataFromRealm() {
        realm.beginTransaction();
        realm.allObjects(VitKHistory.class).clear();
        realm.allObjects(HearingHistory.class).clear();
        realm.allObjects(NbstHistory.class).clear();
        realm.allObjects(FeedingHistory.class).clear();
        realm.allObjects(ServiceUser.class).clear();
        realm.allObjects(Pregnancy.class).clear();
        realm.allObjects(Baby.class).clear();
        realm.allObjects(AntiDHistory.class).clear();
        realm.commitTransaction();
    }
}
