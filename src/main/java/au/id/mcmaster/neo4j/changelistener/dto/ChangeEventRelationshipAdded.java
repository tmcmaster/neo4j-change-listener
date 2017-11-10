package au.id.mcmaster.neo4j.changelistener.dto;

public class ChangeEventRelationshipAdded extends ChangeEvent {
	public ChangeEventRelationshipAdded(long id) {
		super(Type.RELATIONSHIP_ADDED,id);
	}
	public ChangeEventRelationshipAdded(Type type, long id) {
		super(type,id);
	}
}
