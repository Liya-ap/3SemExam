package dat.dao;

import dat.entity.Trip;

import java.util.Set;

public interface ITripGuideDAO {

    void addGuideToTrip(Long tripId, Long guideId);

    Set<Trip> getTripsByGuide(Long guideId);

}
