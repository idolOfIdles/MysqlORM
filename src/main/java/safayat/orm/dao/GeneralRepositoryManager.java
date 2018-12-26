package safayat.orm.dao;

/**
 * Created by safayat on 11/27/18.
 */
public class GeneralRepositoryManager {
    private static GeneralRepositoryManager ourInstance = new GeneralRepositoryManager();
    private GeneralRepository generalRepository;

    public static GeneralRepositoryManager getInstance() {
        return ourInstance;
    }

    private GeneralRepositoryManager() {
        generalRepository = new GeneralRepository();
    }

    public GeneralRepository getGeneralRepository() {
        return generalRepository;
    }
}
