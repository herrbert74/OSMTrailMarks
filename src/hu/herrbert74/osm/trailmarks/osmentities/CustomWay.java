package hu.herrbert74.osm.trailmarks.osmentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CustomWay {
	int id;
	ArrayList<Integer> members;
	HashMap<String, String> tags;
	HashSet<Integer> symbols;
	boolean isFullRound;
	
	public CustomWay(){
		id = 0;
		members = new ArrayList<Integer>();
		tags = new HashMap<String, String>();
		symbols = new HashSet<Integer>();
		isFullRound = false;
	}
	
	public boolean isFullRound() {
		return isFullRound;
	}

	public void setFullRound(boolean isFullRound) {
		this.isFullRound = isFullRound;
	}

	public int getWayId() {
		return id;
	}
	public void setWayId(int id) {
		this.id = id;
	}
	public ArrayList<Integer> getMembers() {
		return members;
	}
	
	public void addMember(Integer member) {
		members.add(member);
	}
	
	public void addSymbol(int symbol){
		symbols.add(symbol);
	}
	
	public HashSet<Integer> getSymbols(){
		return symbols;
	}
	
	public void setMembers(List<Integer> otherMembers) {
		members =  new ArrayList<Integer> ( otherMembers);
	}
	
	public void addMembers(List<Integer> otherMembers) {
		members.addAll(otherMembers);
	}
	
	public void insertMember(int pos, Integer member) {
		members.add(pos, member);
	}
	
	public void addTag(String K, String V){
		tags.put(K, V);
	}
	
	public HashMap<String, String> getTags() {
		return tags;
	}

	public int containsNode(int nodeId){
		int result = -1;
		for(int i = 0; i < members.size(); i++){
			if(nodeId == members.get(i)){
				result = i;
			}
		}
		return result;
	}
}
