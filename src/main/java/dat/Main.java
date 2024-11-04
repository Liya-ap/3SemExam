package dat;

import dat.config.AppConfig;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("exam_db");

        AppConfig.startServer(7070, emf);
    }
}