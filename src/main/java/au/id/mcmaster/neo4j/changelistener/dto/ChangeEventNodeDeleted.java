package au.id.mcmaster.neo4j.changelistener.dto;

public class ChangeEventNodeDeleted extends ChangeEvent {
	public ChangeEventNodeDeleted(long id) {
		super(Type.NODE_DELETED,id);
	}
}
