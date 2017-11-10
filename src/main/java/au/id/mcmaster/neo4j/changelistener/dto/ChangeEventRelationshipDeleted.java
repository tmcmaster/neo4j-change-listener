package au.id.mcmaster.neo4j.changelistener.dto;

public class ChangeEventRelationshipDeleted extends ChangeEvent {
	public ChangeEventRelationshipDeleted(long id) {
		super(Type.RELATIOINSHIP_DELETED,id);
	}
}
