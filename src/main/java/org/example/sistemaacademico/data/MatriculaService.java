package org.example.sistemaacademico.data;


import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MatriculaService {
    private static final String insertarMatricula = "{call insertarMatricula (?,?)}";
    private static final String modificarMatricula = "{call modificarMatricula (?,?,?,?)}";
    private static final String eliminarMatricula = "{call eliminarMatricula(?)}";
    private static final String listarMatriculasPorAlumno = "{?=call listarMatriculasPorAlumno(?)}";
    private static final String listarMatriculasPorAlumnoYCiclo = "{?=call listarMatriculasPorAlumnoYCiclo(?,?)}";
    private Servicio servicio;

    public MatriculaService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarMatricula);
            pstmt.setLong(1, matricula.getPkAlumno());
            pstmt.setLong(2, matricula.getPkGrupo());
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
    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarMatricula);
            pstmt.setLong(1, matricula.getIdMatricula());
            pstmt.setLong(2, matricula.getPkAlumno());
            pstmt.setLong(3, matricula.getPkGrupo());
            pstmt.setLong(4, matricula.getNota());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            System.out.println("\nModificaci�n Satisfactoria!");
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
    public void eliminarMatricula(Long matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarMatricula);
            pstmt.setLong(1, matricula);
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
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumno(String cedula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No se ha localizado el driver o base de datos no disponible");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<MatriculaAlumnoDto> listaMatriculas = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(listarMatriculasPorAlumno);
            cs.registerOutParameter(1, -10);
            cs.setString(2, cedula);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                MatriculaAlumnoDto m = new MatriculaAlumnoDto(
                        rs.getLong("id_matricula"),
                        rs.getDouble("nota"),
                        rs.getString("numero_grupo"),
                        rs.getString("horario"),
                        rs.getString("codigo_carrera"),
                        rs.getString("nombre_carrera"),
                        rs.getString("codigo_curso"),
                        rs.getString("nombre_curso"),
                        rs.getString("nombre_profesor"),
                        rs.getString("cedula_profesor")
                );
                listaMatriculas.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en sentencia SQL");
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (listaMatriculas.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaMatriculas;
    }
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumnoYCiclo(Long alumno, Long ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No hay datos");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<MatriculaAlumnoDto> listaMatriculas = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(listarMatriculasPorAlumnoYCiclo);
            cs.registerOutParameter(1, -10);
            cs.setLong(2, alumno);
            cs.setLong(3, ciclo);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                MatriculaAlumnoDto m = new MatriculaAlumnoDto(
                        rs.getLong("id_matricula"),
                        rs.getDouble("nota"),
                        rs.getString("numero_grupo"),
                        rs.getString("horario"),
                        rs.getString("codigo_carrera"),
                        rs.getString("nombre_carrera"),
                        rs.getString("codigo_curso"),
                        rs.getString("nombre_curso"),
                        rs.getString("nombre_profesor"),
                        rs.getString("cedula_profesor")
                );
                listaMatriculas.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en sentencia SQL");
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (listaMatriculas.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaMatriculas;
    }
}
