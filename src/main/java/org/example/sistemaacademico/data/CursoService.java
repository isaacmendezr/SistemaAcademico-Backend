package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {
    private static final String insertarCurso = "{call insertarCurso (?,?,?,?)}";
    private static final String modificarCurso = "{call modificarCurso (?,?,?,?,?)}";
    private static final String eliminarCurso = "{call eliminarCurso(?)}";
    private static final String listarCursos = "{?=call listarCursos()}";
    private static final String buscarCursoPorNombre = "{?=call buscarCursoPorNombre(?)}";
    private static final String buscarCursoPorCodigo = "{?=call buscarCursoPorCodigo(?)}";
    private static final String buscarCursosPorCarrera = "{?=call buscarCursosPorCarrera(?)}";
    private static final String buscarCursosPorCarreraYCiclo = "{?=call buscarCursosPorCarreraYCiclo (?,?)}";
    private static final String buscarCursosPorCiclo = "{?=call buscarCursosPorCiclo (?)}";
    private Servicio servicio;

    public CursoService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarCurso(Curso curso) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarCurso);
            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setLong(3, curso.getCreditos());
            pstmt.setLong(4, curso.getHorasSemanales());
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
    public void modificarCurso(Curso curso) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarCurso);
            pstmt.setLong(1, curso.getIdCurso());
            pstmt.setString(2, curso.getCodigo());
            pstmt.setString(3, curso.getNombre());
            pstmt.setLong(4, curso.getCreditos());
            pstmt.setLong(5, curso.getHorasSemanales());
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
    public void eliminarCurso(Long curso) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarCurso);
            pstmt.setLong(1, curso);
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
    public List<Curso> listarCursos() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Curso> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarCursos);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Curso(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales")
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
    public Curso buscarCursoPorNombre(String nombre) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Curso curso = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarCursoPorNombre);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, nombre);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                curso = new Curso(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales")
                );
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

        return curso;
    }
    public Curso buscarCursoPorCodigo(String codigo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Curso curso = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarCursoPorCodigo);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, codigo);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                curso = new Curso(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales")
                );
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

        return curso;
    }
    public List<CursoDto> buscarCursosPorCarrera(Long idCarrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        CallableStatement pstmt = null;
        List<CursoDto> cursos = new ArrayList<>();

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarCursosPorCarrera);
            pstmt.registerOutParameter(1, -10);
            pstmt.setLong(2, idCarrera);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                cursos.add(new CursoDto(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales"),
                        rs.getLong("id_carrera_curso"),
                        rs.getLong("anio"),
                        rs.getLong("numero"),
                        rs.getLong("id_ciclo")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }

        if (cursos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return cursos;
    }
    public List<CursoDto> buscarCursosPorCarreraYCiclo(Long carrera, Long ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No se ha localizado el driver o base de datos no disponible");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<CursoDto> listaCursos = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(buscarCursosPorCarreraYCiclo);
            cs.registerOutParameter(1, -10);
            cs.setLong(2, carrera);
            cs.setLong(3, ciclo);
            cs.execute();
            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                CursoDto c = new CursoDto(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales"),
                        rs.getLong("id_carrera_curso")
                );
                listaCursos.add(c);
            }
        }  catch (SQLException e) {
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

        if (listaCursos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaCursos;
    }
    public List<CursoDto> buscarCursosPorCiclo(Long ciclo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No se ha localizado el driver o base de datos no disponible");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<CursoDto> listaCursos = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(buscarCursosPorCiclo);
            cs.registerOutParameter(1, -10);
            cs.setLong(2, ciclo);
            cs.execute();
            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                CursoDto c = new CursoDto(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales"),
                        rs.getLong("id_carrera_curso")
                );
                listaCursos.add(c);
            }
        }  catch (SQLException e) {
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

        if (listaCursos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaCursos;
    }
}
