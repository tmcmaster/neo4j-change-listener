package au.id.mcmaster.neo4j.changelistener.dto;

public class ChangeEventNodeAdded extends ChangeEvent {
	public ChangeEventNodeAdded() {
		super();
	}
	
	public ChangeEventNodeAdded(long id) {
		super(Type.NODE_ADDED, id);
	}
	
	public ChangeEventNodeAdded(Type type, long id) {
		super(type,id);
	}
}
