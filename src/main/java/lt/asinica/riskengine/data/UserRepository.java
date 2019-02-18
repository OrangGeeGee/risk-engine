package lt.asinica.riskengine.data;

/**
 * Usually this would extend CrudRepository for a simple persisted storage.
 */
public interface UserRepository {
    User findOne(Integer id);

    void save(User user);
}
