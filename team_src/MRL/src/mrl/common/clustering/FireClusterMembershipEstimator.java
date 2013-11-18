package mrl.common.clustering;

import mrl.world.object.MrlBuilding;

/**
 * This implementation of {@link mrl.common.clustering.IClusterMembershipChecker} checks for membership of {@link mrl.world.object.MrlBuilding} objects in
 * fire clusters based on estimated temperature of objects.<br/>
 * Acceptable Object types:
 * <ul>
 *     <li>{@link mrl.world.object.MrlBuilding}</li>
 * </ul>
 *
 * @see FireClusterMembershipChecker
 * @author Siavash
 */
public class FireClusterMembershipEstimator implements IClusterMembershipChecker {

    /**
     * Checks membership of the provided {@code object} in any fire cluster. Any {@link mrl.world.object.MrlBuilding} with estimated temperature higher
     * than 25 are considered members.
     *
     * @param object Object to be checked for membership. object must be of type: {@link mrl.world.object.MrlBuilding}.
     * @return true if provided {@link mrl.world.object.MrlBuilding} object is considered member of a cluster, false otherwise.
     * @throws IllegalArgumentException if object is null or not of type: {@link mrl.world.object.MrlBuilding}.
     */
    @Override
    public boolean checkMembership(Object object) {
        boolean member = false;
        MrlBuilding building = null;
        if(object instanceof MrlBuilding){
            building = (MrlBuilding) object;
            if( building.getSelfBuilding() != null
                    && building.getEstimatedFieryness() != 8
                    && building.getEstimatedTemperature() > 25)  {
                member = true;
            }
        } else{
            throw new IllegalArgumentException("Argument object of type:" + object.getClass() + " is illegal.");
        }
        return member;
    }

}
