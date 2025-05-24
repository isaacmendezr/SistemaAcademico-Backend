package org.example.sistemaacademico.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Ciclo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CicloService {
    private static final String insertarCiclo= "{call insertarCiclo (?,?,?,?,?)}";
    private static final String modificarCiclo= "{call modificarCiclo(?,?,?,?,?,?)}";
    private static final String eliminarCiclo = "{call eliminarCiclo(?,?)}";
    private static final String listarCiclos = "{?=call listarCiclos()}";
    private static final String buscarPorAnnio = "{?=call buscarCicloPorAnnio(?)}";
    private static final String activarCiclo= "{call activarCiclo(?)}";
    private Servicio servicio;

    public CicloService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarCiclo);
            pstmt.setLong(1, ciclo.getAnio());
            pstmt.setLong(2, ciclo.getNumero());
            pstmt.setDate(3, java.sql.Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(4, java.sql.Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(5, ciclo.getEstado());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizo la inserci�n");
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
            throw new GlobalException("Llave duplicada");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

    }
    public void modificarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarCiclo);
            pstmt.setLong(1, ciclo.getIdCiclo());
            pstmt.setLong(2, ciclo.getAnio());
            pstmt.setLong(3, ciclo.getNumero());
            pstmt.setDate(4, java.sql.Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(5, java.sql.Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(6, ciclo.getEstado());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            //System.out.println("\nModificaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void eliminarCiclo(Long ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarCiclo);
            pstmt.setLong(1, ciclo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo el borrado");
            }

            //System.out.println("\nEliminaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

    }
    public List<Ciclo> listarCiclos() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Ciclo> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarCiclos);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Ciclo(
                        rs.getLong("id_ciclo"),
                        rs.getLong("anio"),
                        rs.getLong("numero"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getString("activo")
                ));
            }
        } catch (SQLException var16) {
            var16.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var13) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        if (coleccion != null && coleccion.size() != 0) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }
    public Ciclo buscarCicloPorAnnio(Long annio) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Ciclo ciclo = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarPorAnnio);
            pstmt.registerOutParameter(1, -10);
            pstmt.setLong(2, annio);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                ciclo = new Ciclo(
                        rs.getLong("id_ciclo"),
                        rs.getLong("anio"),
                        rs.getLong("numero"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getString("estado"));
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var14) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        return ciclo;
    }
    public void activarCiclo(Long ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(activarCiclo);
            pstmt.setLong(1, ciclo);

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            //System.out.println("\nModificaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
}
