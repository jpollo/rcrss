package mrl.platoon.genericsearch;

import mrl.common.CommandException;
import mrl.common.MRLConstants;
import mrl.helper.BuildingHelper;
import mrl.platoon.MrlPlatoonAgent;
import mrl.world.MrlWorld;
import mrl.world.object.MrlBuilding;
import mrl.world.routing.path.Path;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.entities.StandardEntity;

import java.util.ArrayList;
import java.util.List;

import static mrl.platoon.genericsearch.SearchStatus.CANCELED;
import static mrl.platoon.genericsearch.SearchStatus.FINISHED;

/**
 * @author Siavash
 * @see SearchStrategy
 */
public class SenseSearchStrategy extends SearchStrategy {

    private static Log logger = LogFactory.getLog(SenseSearchStrategy.class);
    public static List<MrlBuilding> SHOULD_CHECK_INSIDE_BUILDINGS = new ArrayList<MrlBuilding>();
    List<Area> buildings;
    int tryCount;
    Area target;
    List<Building> shouldVisitInsideBuildings;
    private Area destination;
    private Area lastTarget;
    private boolean searchUnvisited;


    public SenseSearchStrategy(MrlPlatoonAgent agent, MrlWorld world) {
        super(agent, world);
        buildings = new ArrayList<Area>();
        shouldVisitInsideBuildings = new ArrayList<Building>();
        target = null;
        tryCount = 5;
        if (MRLConstants.LAUNCH_VIEWER) {
            SHOULD_CHECK_INSIDE_BUILDINGS = new ArrayList<MrlBuilding>(world.getShouldCheckInsideBuildings());
        }
        destination = null;
        lastTarget = null;
    }

    private void updateBuildings(Path path) {
        logger.debug("Updating Buildings list...");
        buildings.clear();
        if (path != null && !path.getBuildings().isEmpty()) {
            buildings.addAll(path.getBuildings());
        }
    }


    @Override
    public SearchStatus manualMoveToSearchingBuilding(Building targetBuildingToSearch, boolean searchInside, boolean searchUnvisitedBuildings) throws CommandException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see SenseSearchStrategy#search(boolean, boolean)
     */
    @Override
    public SearchStatus search(boolean inPartition, boolean searchUnvisited) throws CommandException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see SenseSearchStrategy#searchPath()
     */
    @Override
    public SearchStatus searchPath() throws CommandException {
        StandardEntity myPosition = world.getSelfPosition();

        if (myPosition.equals(target) || target == null) {
            target = null;
            for (Area b : buildings) {
                MrlBuilding mrlBuilding = world.getMrlBuilding(b.getID());
                if (/*!(mrlBuilding.isSensed() && world.getTime() - mrlBuilding.getSensedTime() < 15) && */!blackList.contains(b)) {
                    target = b;
                    break;
                }
            }
        }

        if (target != null) {
            if (tryCount > 0) {
                searchBuilding((Building) target);
                tryCount--;
            } else {
                tryCount = 5;
                target = null;
            }
        } else {
            return FINISHED;
        }
        return CANCELED;
    }

    /**
     * @see SenseSearchStrategy#searchBuilding(rescuecore2.standard.entities.Building)
     */
    @Override
    public SearchStatus searchBuilding(Building building) throws CommandException {
        if (building == null) {
            return CANCELED;
        }

//        boolean searchInside = shouldICheckInside(building);
        searchInside = false;
        if (world.getBuildingSeen().contains(building)) {
            if (MRLConstants.DEBUG_SEARCH) {
                world.printData("i've sensed " + building + " search finished!");
            }
            blackList.clear();
            return FINISHED;
        }
        List<Road> entranceRoads = BuildingHelper.getEntranceRoads(world, building); //todo replace it with positions which agent can see this building
        entranceRoads.removeAll(blackList);
        for (Road entranceRoad : entranceRoads) {
            destination = entranceRoad;
            agent.move(entranceRoad, MRLConstants.IN_TARGET, false);
            blackList.add(entranceRoad);
        }

        blackList.add(building);
        return CANCELED;
    }

    /**
     * @see SenseSearchStrategy#search()
     */
    @Override
    public SearchStatus search() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSearchingPath(Path searchingPath, boolean searchUnvisited) {
        this.path = searchingPath;
        if (this.path != null) {
            updateBuildings(searchingPath);
        }
    }

    @Override
    public void setSearchingBuilding(Building searchingBuilding) {
        this.building = searchingBuilding;
    }

    @Override
    public void setSearchUnvisited(boolean searchUnvisited) {
        this.searchUnvisited = searchUnvisited;
    }
}