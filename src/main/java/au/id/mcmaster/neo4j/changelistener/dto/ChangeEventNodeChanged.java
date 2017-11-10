package au.id.mcmaster.neo4j.changelistener.dto;


public class ChangeEventNodeChanged extends ChangeEventNodeAdded {
	public ChangeEventNodeChanged() {
		super();
	}
	public ChangeEventNodeChanged(long id) {
		super(Type.NODE_CHANGED ,id);
	}
}
