package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraCursoService {

    private static final String insertarCursoACarrera = "{call insertarCursoACarrera(?,?,?)}";
    private static final String eliminarCursoDeCarrera = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String modificarOrdenCursoCarrera = "{call modificarOrdenCursoCarrera(?,?,?)}";
    private static final String buscarCursosPorCarreraYCiclo = "{?=call buscarCursosPorCarreraYCiclo(?,?)}";
    private static final String listarCarreraCurso = "{?=call listarCarreraCurso()}";

    private final Servicio servicio;

    public CarreraCursoService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }

    public void insertar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try {
            servicio.conectar();
            CallableStatement cs = servicio.conexion.prepareCall(insertarCursoACarrera);
            cs.setLong(1, carreraCurso.getPkCarrera());
            cs.setLong(2, carreraCurso.getPkCurso());
            cs.setLong(3, carreraCurso.getPkCiclo());

            boolean resultado = cs.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }

            cs.close();
            servicio.desconectar();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GlobalException("Error al insertar CarreraCurso");
        }
    }

    public void eliminar(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        try {
            servicio.conectar();
            CallableStatement cs = servicio.conexion.prepareCall(eliminarCursoDeCarrera);
            cs.setLong(1, idCarrera);
            cs.setLong(2, idCurso);

            int resultado = cs.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado");
            }

            cs.close();
            servicio.desconectar();
        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar CarreraCurso");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try {
            servicio.conectar();
            CallableStatement cs = servicio.conexion.prepareCall(modificarOrdenCursoCarrera);
            cs.setLong(1, carreraCurso.getPkCarrera());
            cs.setLong(2, carreraCurso.getPkCurso());
            cs.setLong(3, carreraCurso.getPkCiclo());

            int resultado = cs.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }

            cs.close();
            servicio.desconectar();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GlobalException("Error al modificar CarreraCurso");
        }
    }

    public List<CursoDto> buscarCursosPorCarreraYCiclo(Long idCarrera, Long idCiclo) throws GlobalException, NoDataException {
        List<CursoDto> cursos = new ArrayList<>();
        ResultSet rs = null;

        try {
            servicio.conectar();
            CallableStatement cs = servicio.conexion.prepareCall(buscarCursosPorCarreraYCiclo);
            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.setLong(2, idCarrera);
            cs.setLong(3, idCiclo);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {
                CursoDto dto = new CursoDto(
                        rs.getLong("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getLong("creditos"),
                        rs.getLong("horas_semanales"),
                        rs.getLong("id_carrera_curso")
                );
                cursos.add(dto);
            }

            rs.close();
            cs.close();
            servicio.desconectar();
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar cursos por carrera y ciclo");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos para la carrera y ciclo indicados");
        }

        return cursos;
    }

    public List<CarreraCurso> listar() throws GlobalException, NoDataException {
        List<CarreraCurso> lista = new ArrayList<>();
        ResultSet rs = null;

        try {
            servicio.conectar();
            CallableStatement cs = servicio.conexion.prepareCall(listarCarreraCurso);
            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                CarreraCurso cc = new CarreraCurso(
                        rs.getLong("id_carrera_curso"),
                        rs.getLong("pk_carrera"),
                        rs.getLong("pk_curso"),
                        rs.getLong("pk_ciclo")
                );
                lista.add(cc);
            }

            rs.close();
            cs.close();
            servicio.desconectar();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GlobalException("Error al listar CarreraCurso");
        }

        if (lista.isEmpty()) {
            throw new NoDataException("No hay datos de CarreraCurso");
        }

        return lista;
    }
}
