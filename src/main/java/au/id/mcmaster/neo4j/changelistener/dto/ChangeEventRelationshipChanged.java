package au.id.mcmaster.neo4j.changelistener.dto;

public class ChangeEventRelationshipChanged extends ChangeEventRelationshipAdded {
	public ChangeEventRelationshipChanged(long id) {
		super(Type.RELATIONSHIP_CHANGED, id);
	}
}