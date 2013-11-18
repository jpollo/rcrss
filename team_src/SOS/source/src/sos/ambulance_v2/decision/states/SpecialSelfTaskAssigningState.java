package sos.ambulance_v2.decision.states;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import sos.ambulance_v2.AmbulanceInformationModel;
import sos.ambulance_v2.AmbulanceTeamAgent;
import sos.ambulance_v2.AmbulanceUtils;
import sos.ambulance_v2.decision.AmbulanceDecision;
import sos.base.entities.AmbulanceTeam;
import sos.base.entities.Building;
import sos.base.entities.Human;
import sos.base.entities.StandardEntity;
import sos.base.message.structure.SOSBitArray;
import sos.base.message.structure.blocks.DataArrayList;
import sos.base.message.structure.channel.Channel;
import sos.base.util.geom.ShapeInArea;
import sos.tools.decisionMaker.definitions.commands.SOSTask;
import sos.tools.decisionMaker.implementations.stateBased.SOSEventPool;
import sos.tools.decisionMaker.implementations.stateBased.events.SOSEvent;
import sos.tools.decisionMaker.implementations.stateBased.states.SOSIState;
import sos.tools.decisionMaker.implementations.tasks.SaveHumanTask;

/* 
 * @author reyhaneh
 */
public class SpecialSelfTaskAssigningState extends SOSIState {

	AmbulanceDecision ambDecision;
	private AmbulanceTeamAgent ambulance =null;
	
	public SpecialSelfTaskAssigningState(AmbulanceInformationModel infoModel) {
		super(infoModel);
		ambulance = infoModel.getAmbulance();
		this.ambDecision=infoModel.getAmbulance().ambDecision;
	}

	@Override
	public SOSTask<?> decide(SOSEventPool eventPool) {

		infoModel.getLog().info("$$$$$$$$$$$$$$$$ SpecialSelfTaskAssigningState $$$$$$$$$$$$$$$$$$$$$$");
		ambDecision.dclog.info("******************** SpecialSelfTaskAssigningState ***************");
		
		ArrayList<Human> targets = getSensedAmbulanceAndTargets();
		
		if ( !isValid(targets) ){
			infoModel.getLog().info("$$$$$ Skipped from SelfTaskAssigningState $$$$$");
			return null;
		}
		ambDecision.dclog.info("specialTaskAssigning targets = " + targets);
		ambDecision.updateHumansInfo();	
		
		Human target = findNearestTarget(targets);
		
		if(target == null){
			infoModel.getLog().info("in specialSelfTaskAssigning class Target = null ");
			return null;
		}
		
		AmbulanceUtils.updateATtarget(target,me(),this);
		infoModel.getLog().info(" AT =" + infoModel.getAgent().getID() + "    nearest target =" + target);
		ambDecision.dclog.info(" WorkTarget = " + me().getWork().getTarget());
		ambulance.lastState = " SpecialSelfTaskAssigningState ";
		
		ambulance.currentSaveHumanTask = new SaveHumanTask(target, infoModel.getTime());
			return ambulance.currentSaveHumanTask;
	}
	

	private Human findNearestTarget(ArrayList<Human> targets ) {
		ambDecision.dclog.info("******** findNearestTarget ***********");

		Human nearestTarget = null;
		int minDistance = Integer.MAX_VALUE;
		
		for ( Human human : targets ) {
		
			int distance = (int) Point2D.distance(human.getX(), human.getY(), me().getX(), me().getY()); 
			infoModel.getLog().info(" in findNearestTarget :  target=  "+ human + "  distance= "+ distance);
			ambDecision.dclog.info("Human = "+ human + "Distance = "+ distance);
					
			if( nearestTarget == null){
				nearestTarget = human;
				minDistance = distance;
				continue;
			}
			else if( distance < minDistance){
				nearestTarget = human;
				minDistance = distance;
			}
			
		}
		ambDecision.dclog.info("***************************************");

		return nearestTarget;
	}

	private boolean isValid(ArrayList<Human> targets) {
		
		if (targets == null || targets.isEmpty()) 
			return false;
		
		return true;
	}

	@Override
	public void giveFeedbacks(List feedbacks) {

	}

	@Override
	public void skipped() {

	}

	@Override
	public void overTaken() {
		// TODO Auto-generated method stub

	}

	private ArrayList<Human> getSensedAmbulanceAndTargets() {
		ArrayList<Human> sensedHumans = infoModel.getAgent().getVisibleEntities(Human.class);
		ArrayList<Human> targets = new ArrayList<Human>();

		for (Human hum : sensedHumans) {
			
			if (!AmbulanceUtils.isValidToDecide(hum, infoModel.getLog())) 
					continue;
			if ( hum.getRescueInfo().getDeathTime() < infoModel.getTime() + hum.getBuriedness() + 1)
			    	continue;
		    if ( hum.getBuriedness() > 30 && hum.getRescueInfo().getDeathTime()-infoModel.getTime()>100)
			    	continue;
		    if(! (hum.getPosition() instanceof Building) )
		    		continue;
		    
		    if( cansee(me(), ((Building) hum.getAreaPosition()).getSearchAreas()))
		    		targets.add(hum);
				
		}	
					
		return targets;
	}

	private boolean cansee(AmbulanceTeam ambulanceTeam, ArrayList<ShapeInArea> searchAreas) {
		for (ShapeInArea shapeInArea : searchAreas) {
			if (shapeInArea.contains(ambulanceTeam.getPositionPoint().toGeomPoint()))
				return true;
		}
		return false;
	}

	private AmbulanceTeam me(){
		return (AmbulanceTeam) (infoModel.getAgent().me());
	}

	@Override
	protected void handleEvent(SOSEvent sosEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hear(String header, DataArrayList data, SOSBitArray dynamicBitArray, StandardEntity sender, Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	
}
