package ie.teamchile.smartapp.activities.CreateAppointment;

import java.util.List;

import ie.teamchile.smartapp.activities.Base.BaseViewSec;
import ie.teamchile.smartapp.model.ServiceUser;

/**
 * Created by elliot on 01/01/2016.
 */
public interface CreateAppointmentView extends BaseViewSec {
    void userSearchDialog(String title, List<ServiceUser> serviceUsers);

    void gotoHomeVisitAppointment();

    void gotoClinicAppointment();
}
