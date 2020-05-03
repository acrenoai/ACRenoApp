package rs.acreno.racuni;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteDataSource;
import rs.acreno.system.DAO.SqlQuerys;
import rs.acreno.system.constants.Constants;
import rs.acreno.system.exeption.AcrenoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLRacuniDAO implements RacuniDAO {
    private Connection connection;
    private final QueryRunner dbAccess = new QueryRunner();
    private List<Racun> racunList = null;

    @Override
    public Connection connect() throws SQLException {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(Constants.MSACCESS_STRING_URL);
        connection = ds.getConnection();
        return connection = DriverManager.getConnection(Constants.MSACCESS_STRING_URL);
    }

    @Override
    public void close() throws AcrenoException, SQLException {
        connection.close();
        try {
            DriverManager.getConnection(Constants.MSACCESS_STRING_URL);
        } catch (Exception e) {
            throw new AcrenoException("Greska u DB close RACUNI", e);
        }
    }

    @Override
    public long insertRacun(@NotNull Racun racun) throws AcrenoException, SQLException {
        try {
            connect();
            dbAccess.update(connection,
                    SqlQuerys.INSERT_RACUN_IN_TABLE
                    , racun.getIdRacuna()
                    , racun.getIdAutomobila()
                    , racun.getDatum()
                    , racun.getPopust()
                    , racun.getNapomeneRacuna());

        } catch (SQLException e) {
            throw new AcrenoException("Greska u DB insertRacun RACUNI", e);
        }
        close();
        return -1L;
    }

    @Override
    public boolean updateRacun(@NotNull Racun racun) throws SQLException, AcrenoException {
        try {
            connect();
            dbAccess.update(connection, SqlQuerys.UPDATE_RACUN_TABLE
                    , racun.getIdAutomobila()
                    , racun.getDatum()
                    , racun.getPopust()
                    , racun.getNapomeneRacuna()
                    , racun.getIdRacuna());
            return true;
        } catch (SQLException e) {
            throw new AcrenoException("Greska u DB updateRacun RACUNI", e);
        } finally {
            if (connection != null) {
                close();
            }
        }
    }

    @Override
    public boolean deleteRacun(Racun racun) throws AcrenoException, SQLException {
        try {
            connect();
            dbAccess.update(connection, SqlQuerys.DELETE_FROM_RACUN, racun.getIdRacuna());
            return true;
        } catch (Exception e) {
            throw new AcrenoException("Greska u DB DELETE deleteRacun RACUNI", e);
        } finally {
            if (connection != null) {
                close();
            }
        }
    }

    @Override
    public List<Racun> findRacunByProperty(@NotNull RacuniSearchType racuniSearchType, Object value)
            throws AcrenoException, SQLException {
        String whereClause = "";
        String valueClause = "";
        switch (racuniSearchType) {
            case ID_RACUNA -> {
                whereClause = "idRacuna LIKE ?";
                valueClause = "%" + value.toString() + "%";
            }
            case ID_AUTOMOBILA -> {
                whereClause = "IdAutomobila LIKE ?";
                valueClause = "%" + value.toString() + "%";
            }
            case DATUM_RACUNA -> {
                whereClause = "datum LIKE ?";
                valueClause = "%" + value.toString() + "%";
            }
            default -> System.out.println("Nepoznati Search type RACUNI");
        }
        try {
            connect();
            racunList = dbAccess.query(connection, SqlQuerys.FIND_ALL_RACUNE_BY_PROPERTY +
                    whereClause, new BeanListHandler<>(Racun.class), valueClause);
        } catch (Exception e) {
            throw new AcrenoException("Greska u DB findRacunByProperty RACUNA", e);
        } finally {
            if (connection != null) {
                close();
            }
        }
        return racunList;
    }

    @Override
    public List<Racun> findAllRacune() throws AcrenoException, SQLException {
        try {
            connect();
            racunList = dbAccess.query(connection, SqlQuerys.FIND_ALL_RACUNE,
                    new BeanListHandler<>(Racun.class));
        } catch (SQLException e) {
            throw new AcrenoException("Greska u DB findAllRacune RACUNA", e);
        } finally {
            if (connection != null) {
                close();
            }
        }
        return racunList;
    }
}
