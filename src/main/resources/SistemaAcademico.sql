SET SERVEROUTPUT ON;

----------------------------------------------------------------------------------------------------------
-- LIMPIEZA OPCIONAL DE TABLAS Y OBJETOS EXISTENTES
----------------------------------------------------------------------------------------------------------

-- Procedimientos y funciones
-- CARRERA
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarCarreras'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCarreraPorCodigo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCarreraPorNombre'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- CURSO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarCurso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarCurso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarCurso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarCursos'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCursoPorNombre'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCursoPorCodigo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCursosPorCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCursosPorCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- CICLO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarCiclos'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCicloPorAnio'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE activarCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE buscarCicloPorId'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- CARRERA_CURSO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarCursoACarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarCursoDeCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarOrdenCursoCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarCursosPorCarreraYCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarCarreraCurso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- PROFESOR
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarProfesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarProfesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarProfesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarProfesorPorCedula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarProfesores'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarProfesorPorCedula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarProfesorPorNombre'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- ALUMNO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarAlumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarAlumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarAlumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarAlumnoPorCedula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarAlumnos'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarAlumnoPorCedula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarAlumnoPorNombre'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarAlumnosPorCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarAlumnosConOfertaEnCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- GRUPO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarGrupo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarGrupo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarGrupo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarGrupos'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarGruposPorCarreraCurso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarGruposPorCursoCicloCarrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarGruposPorProfesorCicloActivo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarGruposPorProfesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- MATRICULA
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarMatricula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarMatricula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarMatricula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE listarMatriculasPorAlumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE listarMatriculasPorAlumnoYCiclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- USUARIO
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE insertarUsuario'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE modificarUsuario'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE eliminarUsuario'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION listarUsuarios'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION buscarUsuarioPorCedula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PROCEDURE loginUsuario'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- PACKAGE
BEGIN EXECUTE IMMEDIATE 'DROP PACKAGE Types'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- Tablas
DROP TABLE Matricula CASCADE CONSTRAINTS;
DROP TABLE Grupo CASCADE CONSTRAINTS;
DROP TABLE Alumno CASCADE CONSTRAINTS;
DROP TABLE Profesor CASCADE CONSTRAINTS;
DROP TABLE Carrera_Curso CASCADE CONSTRAINTS;
DROP TABLE Ciclo CASCADE CONSTRAINTS;
DROP TABLE Curso CASCADE CONSTRAINTS;
DROP TABLE Carrera CASCADE CONSTRAINTS;
DROP TABLE Usuario CASCADE CONSTRAINTS;

-- Triggers
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_carrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_curso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_ciclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_grupo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_alumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_profesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_validar_ciclo_fecha'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_validar_datos_alumno'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_validar_datos_profesor'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_duplicate_carrera_curso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_duplicate_grupo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_duplicate_usuario'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_duplicate_matricula'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_restrict_matricula_carrera'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_validate_matricula_nota'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
/*BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_ensure_single_active_ciclo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_validate_usuario_tipo'; EXCEPTION WHEN OTHERS THEN NULL; END;
/*/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_prevent_delete_carrera_curso'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

COMMIT;

----------------------------------------------------------------------------------------------------------
-- CREACIÓN DE ESTRUCTURA DE BASE DE DATOS (TABLAS Y OBJETOS PL/SQL)
----------------------------------------------------------------------------------------------------------

-- TABLAS

CREATE TABLE Carrera (
                         id_carrera NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         codigo VARCHAR2(10) UNIQUE,
                         nombre VARCHAR2(100),
                         titulo VARCHAR2(100)
);

CREATE TABLE Curso (
                       id_curso NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       codigo VARCHAR2(10) UNIQUE,
                       nombre VARCHAR2(100),
                       creditos NUMBER,
                       horas_semanales NUMBER
);

CREATE TABLE Ciclo (
                       id_ciclo NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       anio NUMBER,
                       numero NUMBER CHECK (numero IN (1, 2)),
                       fecha_inicio DATE,
                       fecha_fin DATE,
                       estado VARCHAR2(10) CHECK (estado IN ('Activo', 'Inactivo'))
);

CREATE TABLE Carrera_Curso (
                               id_carrera_curso NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                               pk_carrera NUMBER,
                               pk_curso NUMBER,
                               pk_ciclo NUMBER,
                               FOREIGN KEY (pk_carrera) REFERENCES Carrera(id_carrera),
                               FOREIGN KEY (pk_curso) REFERENCES Curso(id_curso),
                               FOREIGN KEY (pk_ciclo) REFERENCES Ciclo(id_ciclo)
);

CREATE TABLE Profesor (
                          id_profesor NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          cedula VARCHAR2(15) UNIQUE,
                          nombre VARCHAR2(100),
                          telefono VARCHAR2(20),
                          email VARCHAR2(100) UNIQUE
);

CREATE TABLE Alumno (
                        id_alumno NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        cedula VARCHAR2(15) UNIQUE,
                        nombre VARCHAR2(100),
                        telefono VARCHAR2(20),
                        email VARCHAR2(100) UNIQUE,
                        fecha_nacimiento DATE,
                        pk_carrera NUMBER,
                        FOREIGN KEY (pk_carrera) REFERENCES Carrera(id_carrera)
);

CREATE TABLE Grupo (
                       id_grupo NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       pk_carrera_curso NUMBER,
                       numero_grupo NUMBER,
                       horario VARCHAR2(100),
                       pk_profesor NUMBER,
                       FOREIGN KEY (pk_carrera_curso) REFERENCES Carrera_Curso(id_carrera_curso),
                       FOREIGN KEY (pk_profesor) REFERENCES Profesor(id_profesor)
);

CREATE TABLE Matricula (
                           id_matricula NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                           pk_alumno NUMBER,
                           pk_grupo NUMBER,
                           nota NUMBER,
                           FOREIGN KEY (pk_alumno) REFERENCES Alumno(id_alumno),
                           FOREIGN KEY (pk_grupo) REFERENCES Grupo(id_grupo)
);

CREATE TABLE Usuario (
                         id_usuario NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         cedula VARCHAR2(15) UNIQUE,
                         clave VARCHAR2(100),
                         tipo VARCHAR2(20) CHECK (tipo IN ('Administrador', 'Matriculador', 'Profesor', 'Alumno')) NOT NULL
);
COMMIT;

--------------------------------------------------CURSOR--------------------------------------------------

CREATE OR REPLACE PACKAGE Types
AS
    TYPE ref_cursor IS REF CURSOR;
END Types;
/

-------------------------------------------------CARRERAS-------------------------------------------------

-- Insertar Carrera
CREATE OR REPLACE PROCEDURE insertarCarrera(
    codigo IN Carrera.codigo%TYPE,
    nombre IN Carrera.nombre%TYPE,
    titulo IN Carrera.titulo%TYPE)
AS
BEGIN
    INSERT INTO Carrera (codigo, nombre, titulo)
    VALUES (codigo, nombre, titulo);
    COMMIT;
END;
/

-- Modificar Carrera
CREATE OR REPLACE PROCEDURE modificarCarrera(
    id_carrerain IN Carrera.id_carrera%TYPE,
    codigoin IN Carrera.codigo%TYPE,
    nombrein IN Carrera.nombre%TYPE,
    tituloin IN Carrera.titulo%TYPE)
AS
BEGIN
    UPDATE Carrera
    SET codigo = NVL(codigoin, codigo),
        nombre = NVL(nombrein, nombre),
        titulo = NVL(tituloin, titulo)
    WHERE id_carrera = id_carrerain;
    COMMIT;
END;
/

-- Eliminar Carrera
CREATE OR REPLACE PROCEDURE eliminarCarrera(id_carrerain IN Carrera.id_carrera%TYPE)
AS
BEGIN
    DELETE FROM Carrera WHERE id_carrera = id_carrerain;
    COMMIT;
END;
/

-- Listar Carreras
CREATE OR REPLACE FUNCTION listarCarreras
    RETURN Types.ref_cursor
AS
    carrera_cursor Types.ref_cursor;
BEGIN
    OPEN carrera_cursor FOR
        SELECT id_carrera, codigo, nombre, titulo FROM Carrera;
    RETURN carrera_cursor;
END;
/

-- Buscar Carrera por codigo
CREATE OR REPLACE FUNCTION buscarCarreraPorCodigo(codigobuscar IN Carrera.codigo%TYPE)
    RETURN Types.ref_cursor
AS
    carrera_cursor Types.ref_cursor;
BEGIN
    OPEN carrera_cursor FOR
        SELECT id_carrera, codigo, nombre, titulo FROM Carrera WHERE codigo = codigobuscar;
    RETURN carrera_cursor;
END;
/

-- Buscar Carrera por nombre
CREATE OR REPLACE FUNCTION buscarCarreraPorNombre(nombrebuscar IN Carrera.nombre%TYPE)
    RETURN Types.ref_cursor
AS
    carrera_cursor Types.ref_cursor;
BEGIN
    OPEN carrera_cursor FOR
        SELECT id_carrera, codigo, nombre, titulo FROM Carrera WHERE UPPER(nombre) LIKE UPPER('%' || nombrebuscar || '%');
    RETURN carrera_cursor;
END;
/

------------------------------------------CARRERAS_CURSO------------------------------------------

-- Insertar curso en carrera
CREATE OR REPLACE PROCEDURE insertarCursoACarrera(
    carrera_id IN Carrera.id_carrera%TYPE,
    curso_id IN Curso.id_curso%TYPE,
    ciclo_id IN Ciclo.id_ciclo%TYPE
)
AS
BEGIN
    INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo)
    VALUES (carrera_id, curso_id, ciclo_id);
    COMMIT;
END;
/

-- Eliminar curso de carrera
CREATE OR REPLACE PROCEDURE eliminarCursoDeCarrera(
    carrera_id IN Carrera.id_carrera%TYPE,
    curso_id IN Curso.id_curso%TYPE
)
AS
BEGIN
    DELETE FROM Carrera_Curso
    WHERE pk_carrera = carrera_id AND pk_curso = curso_id;
    COMMIT;
END;
/

-- Modificar el ciclo de un curso en una carrera(cambiar el orden de un curso)
CREATE OR REPLACE PROCEDURE modificarOrdenCursoCarrera(
    p_pk_carrera IN NUMBER,
    p_pk_curso IN NUMBER,
    p_nuevo_pk_ciclo IN NUMBER
)
AS
BEGIN
    UPDATE Carrera_Curso
    SET pk_ciclo = p_nuevo_pk_ciclo
    WHERE pk_carrera = p_pk_carrera AND pk_curso = p_pk_curso;
    COMMIT;
END;
/

-- Listar cursos por carrera y ciclo
CREATE OR REPLACE FUNCTION buscarCursosPorCarreraYCiclo(
    p_carrera_id IN Carrera.id_carrera%TYPE,
    p_ciclo_id IN Ciclo.id_ciclo%TYPE
)
    RETURN Types.ref_cursor
AS
    v_cursor Types.ref_cursor;
BEGIN
    OPEN v_cursor FOR
        SELECT
            c.id_curso,
            c.codigo,
            c.nombre,
            c.creditos,
            c.horas_semanales,
            cc.id_carrera_curso
        FROM Curso c
                 JOIN Carrera_Curso cc ON c.id_curso = cc.pk_curso
        WHERE cc.pk_carrera = p_carrera_id
          AND cc.pk_ciclo = p_ciclo_id
        ORDER BY c.codigo;
    RETURN v_cursor;
END;
/

-- Listar todas las relaciones Carrera_Curso
CREATE OR REPLACE FUNCTION listarCarreraCurso
    RETURN Types.ref_cursor
AS
    cursor_out Types.ref_cursor;
BEGIN
    OPEN cursor_out FOR
        SELECT id_carrera_curso, pk_carrera, pk_curso, pk_ciclo
        FROM Carrera_Curso;
    RETURN cursor_out;
END;
/

------------------------------------------------CURSOS--------------------------------------------

-- Insertar Curso
CREATE OR REPLACE PROCEDURE insertarCurso(
    codigo IN Curso.codigo%TYPE,
    nombre IN Curso.nombre%TYPE,
    creditos IN Curso.creditos%TYPE,
    horas_semanales IN Curso.horas_semanales%TYPE)
AS
BEGIN
    INSERT INTO Curso (codigo, nombre, creditos, horas_semanales)
    VALUES (codigo, nombre, creditos, horas_semanales);
    COMMIT;
END;
/

-- Modificar Curso
CREATE OR REPLACE PROCEDURE modificarCurso(
    id_cursoin IN Curso.id_curso%TYPE,
    codigoin IN Curso.codigo%TYPE,
    nombrein IN Curso.nombre%TYPE,
    creditosin IN Curso.creditos%TYPE,
    horas_semanalesin IN Curso.horas_semanales%TYPE)
AS
BEGIN
    UPDATE Curso
    SET codigo = NVL(codigoin, codigo),
        nombre = NVL(nombrein, nombre),
        creditos = NVL(creditosin, creditos),
        horas_semanales = NVL(horas_semanalesin, horas_semanales)
    WHERE id_curso = id_cursoin;
    COMMIT;
END;
/

-- Eliminar Curso
CREATE OR REPLACE PROCEDURE eliminarCurso(id_cursoin IN Curso.id_curso%TYPE)
AS
BEGIN
    DELETE FROM Curso WHERE id_curso = id_cursoin;
    COMMIT;
END;
/

-- Listar Cursos
CREATE OR REPLACE FUNCTION listarCursos
    RETURN Types.ref_cursor
AS
    curso_cursor Types.ref_cursor;
BEGIN
    OPEN curso_cursor FOR
        SELECT id_curso, codigo, nombre, creditos, horas_semanales FROM Curso;
    RETURN curso_cursor;
END;
/

-- Buscar Curso por nombre
CREATE OR REPLACE FUNCTION buscarCursoPorNombre(nombrebuscar IN Curso.nombre%TYPE)
    RETURN Types.ref_cursor
AS
    curso_cursor Types.ref_cursor;
BEGIN
    OPEN curso_cursor FOR
        SELECT id_curso, codigo, nombre, creditos, horas_semanales FROM Curso WHERE UPPER(nombre) LIKE UPPER('%' || nombrebuscar || '%');
    RETURN curso_cursor;
END;
/

-- Buscar Curso por código
CREATE OR REPLACE FUNCTION buscarCursoPorCodigo(codigobuscar IN Curso.codigo%TYPE)
    RETURN Types.ref_cursor
AS
    curso_cursor Types.ref_cursor;
BEGIN
    OPEN curso_cursor FOR
        SELECT id_curso, codigo, nombre, creditos, horas_semanales FROM Curso WHERE codigo = codigobuscar;
    RETURN curso_cursor;
END;
/

-- Buscar Curso por carrera
CREATE OR REPLACE FUNCTION buscarCursosPorCarrera(
    p_carrera_id IN Carrera.id_carrera%TYPE
)
    RETURN Types.ref_cursor
AS
    v_cursor Types.ref_cursor;
BEGIN
    OPEN v_cursor FOR
        SELECT
            c.id_curso,
            c.codigo,
            c.nombre,
            c.creditos,
            c.horas_semanales,
            cc.id_carrera_curso,
            ci.anio,
            ci.numero,
            ci.id_ciclo
        FROM Curso c
                 JOIN Carrera_Curso cc ON c.id_curso = cc.pk_curso
                 JOIN Ciclo ci ON cc.pk_ciclo = ci.id_ciclo
        WHERE cc.pk_carrera = p_carrera_id
        ORDER BY ci.anio, ci.numero, c.codigo;
    RETURN v_cursor;
END;
/

-- Listar Cursos por Ciclo
CREATE OR REPLACE FUNCTION buscarCursosPorCiclo(
    p_ciclo_id IN Ciclo.id_ciclo%TYPE
)
    RETURN Types.ref_cursor
AS
    curso_cursor Types.ref_cursor;
BEGIN
    OPEN curso_cursor FOR
        SELECT
            c.id_curso,
            c.codigo,
            c.nombre,
            c.creditos,
            c.horas_semanales,
            cc.id_carrera_curso,
            ci.anio,
            ci.numero,
            ci.id_ciclo
        FROM Curso c
                 JOIN Carrera_Curso cc ON c.id_curso = cc.pk_curso
                 JOIN Ciclo ci ON cc.pk_ciclo = ci.id_ciclo
                 JOIN Carrera ca ON cc.pk_carrera = ca.id_carrera
        WHERE ci.id_ciclo = p_ciclo_id
        ORDER BY ca.nombre, c.codigo;
    RETURN curso_cursor;
END;
/

------------------------------------------------CICLOS--------------------------------------------

-- Insertar Ciclo
CREATE OR REPLACE PROCEDURE insertarCiclo(
    anio IN Ciclo.anio%TYPE,
    numero IN Ciclo.numero%TYPE,
    fecha_inicio IN Ciclo.fecha_inicio%TYPE,
    fecha_fin IN Ciclo.fecha_fin%TYPE,
    estado IN Ciclo.estado%TYPE)
AS
BEGIN
    INSERT INTO Ciclo (anio, numero, fecha_inicio, fecha_fin, estado)
    VALUES (anio, numero, fecha_inicio, fecha_fin, estado);
    COMMIT;
END;
/

-- Modificar Ciclo
CREATE OR REPLACE PROCEDURE modificarCiclo(
    id_cicloin IN Ciclo.id_ciclo%TYPE,
    anioin IN Ciclo.anio%TYPE,
    numeroin IN Ciclo.numero%TYPE,
    fecha_inicioin IN Ciclo.fecha_inicio%TYPE,
    fecha_finin IN Ciclo.fecha_fin%TYPE,
    estadoin IN Ciclo.estado%TYPE)
AS
BEGIN
    UPDATE Ciclo
    SET anio = NVL(anioin, anio),
        numero = NVL(numeroin, numero),
        fecha_inicio = NVL(fecha_inicioin, fecha_inicio),
        fecha_fin = NVL(fecha_finin, fecha_fin),
        estado = NVL(estadoin, estado)
    WHERE id_ciclo = id_cicloin;
    COMMIT;
END;
/

-- Eliminar Ciclo
CREATE OR REPLACE PROCEDURE eliminarCiclo(id_cicloin IN Ciclo.id_ciclo%TYPE)
AS
BEGIN
    DELETE FROM Ciclo WHERE id_ciclo = id_cicloin;
    COMMIT;
END;
/

-- Listar Ciclos
CREATE OR REPLACE FUNCTION listarCiclos
    RETURN Types.ref_cursor
AS
    ciclo_cursor Types.ref_cursor;
BEGIN
    OPEN ciclo_cursor FOR
        SELECT id_ciclo, anio, numero, fecha_inicio, fecha_fin, estado FROM Ciclo;
    RETURN ciclo_cursor;
END;
/

-- Consultar Ciclos por año
CREATE OR REPLACE FUNCTION buscarCicloPorAnio(anioBuscar IN Ciclo.anio%TYPE)
    RETURN Types.ref_cursor
AS
    ciclo_cursor Types.ref_cursor;
BEGIN
    OPEN ciclo_cursor FOR
        SELECT id_ciclo, anio, numero, fecha_inicio, fecha_fin, estado FROM Ciclo
        WHERE anio = anioBuscar;
    RETURN ciclo_cursor;
END;
/

-- Activar ciclo
CREATE OR REPLACE PROCEDURE activarCiclo(id_cicloin IN Ciclo.id_ciclo%TYPE)
AS
BEGIN
    UPDATE Ciclo SET estado = 'Inactivo';
    UPDATE Ciclo SET estado = 'Activo' WHERE id_ciclo = id_cicloin;
    COMMIT;
END;
/

-- Buscar Ciclo por ID
CREATE OR REPLACE FUNCTION buscarCicloPorId(id_cicloin IN Ciclo.id_ciclo%TYPE)
    RETURN Types.ref_cursor
AS
    ciclo_cursor Types.ref_cursor;
BEGIN
    OPEN ciclo_cursor FOR
        SELECT id_ciclo, anio, numero, fecha_inicio, fecha_fin, estado
        FROM Ciclo
        WHERE id_ciclo = id_cicloin;
    RETURN ciclo_cursor;
END;
/

------------------------------------------------PROFESORES--------------------------------------------

-- Insertar Profesor
CREATE OR REPLACE PROCEDURE insertarProfesor(
    cedula IN Profesor.cedula%TYPE,
    nombre IN Profesor.nombre%TYPE,
    telefono IN Profesor.telefono%TYPE,
    email IN Profesor.email%TYPE)
AS
BEGIN
    INSERT INTO Profesor (cedula, nombre, telefono, email)
    VALUES (cedula, nombre, telefono, email);
    COMMIT;
END;
/

-- Modificar Profesor
CREATE OR REPLACE PROCEDURE modificarProfesor(
    id_profesorin IN Profesor.id_profesor%TYPE,
    cedulain IN Profesor.cedula%TYPE,
    nombrein IN Profesor.nombre%TYPE,
    telefonoin IN Profesor.telefono%TYPE,
    emailin IN Profesor.email%TYPE)
AS
BEGIN
    UPDATE Profesor
    SET cedula = NVL(cedulain, cedula),
        nombre = NVL(nombrein, nombre),
        telefono = NVL(telefonoin, telefono),
        email = NVL(emailin, email)
    WHERE id_profesor = id_profesorin;
    COMMIT;
END;
/

-- Eliminar Profesor
CREATE OR REPLACE PROCEDURE eliminarProfesor(id_profesorin IN Profesor.id_profesor%TYPE)
AS
BEGIN
    DELETE FROM Profesor WHERE id_profesor = id_profesorin;
    COMMIT;
END;
/

-- Eliminar Profesor por cédula
CREATE OR REPLACE PROCEDURE eliminarProfesorPorCedula(
    p_cedula IN Profesor.cedula%TYPE
)
AS
BEGIN
    DELETE FROM Profesor WHERE cedula = p_cedula;
    COMMIT;
END;
/

-- Listar Profesores
CREATE OR REPLACE FUNCTION listarProfesores
    RETURN Types.ref_cursor
AS
    profesor_cursor Types.ref_cursor;
BEGIN
    OPEN profesor_cursor FOR
        SELECT id_profesor, cedula, nombre, telefono, email FROM Profesor;
    RETURN profesor_cursor;
END;
/

-- Buscar Profesor por cédula
CREATE OR REPLACE FUNCTION buscarProfesorPorCedula(cedulabuscar IN Profesor.cedula%TYPE)
    RETURN Types.ref_cursor
AS
    profesor_cursor Types.ref_cursor;
BEGIN
    OPEN profesor_cursor FOR
        SELECT id_profesor, cedula, nombre, telefono, email FROM Profesor WHERE cedula = cedulabuscar;
    RETURN profesor_cursor;
END;
/

-- Buscar Profesor por nombre
CREATE OR REPLACE FUNCTION buscarProfesorPorNombre(nombrebuscar IN Profesor.nombre%TYPE)
    RETURN Types.ref_cursor
AS
    profesor_cursor Types.ref_cursor;
BEGIN
    OPEN profesor_cursor FOR
        SELECT id_profesor, cedula, nombre, telefono, email FROM Profesor WHERE UPPER(nombre) LIKE UPPER('%' || nombrebuscar || '%');
    RETURN profesor_cursor;
END;
/

------------------------------------------------ALUMNOS--------------------------------------------

-- Insertar Alumno
CREATE OR REPLACE PROCEDURE insertarAlumno(
    cedula IN Alumno.cedula%TYPE,
    nombre IN Alumno.nombre%TYPE,
    telefono IN Alumno.telefono%TYPE,
    email IN Alumno.email%TYPE,
    fecha_nacimiento IN Alumno.fecha_nacimiento%TYPE,
    pk_carrera IN Alumno.pk_carrera%TYPE)
AS
BEGIN
    INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera)
    VALUES (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera);
    COMMIT;
END;
/

-- Modificar Alumno
CREATE OR REPLACE PROCEDURE modificarAlumno(
    id_alumnoin IN Alumno.id_alumno%TYPE,
    cedulain IN Alumno.cedula%TYPE,
    nombrein IN Alumno.nombre%TYPE,
    telefonoin IN Alumno.telefono%TYPE,
    emailin IN Alumno.email%TYPE,
    fecha_nacimientoin IN Alumno.fecha_nacimiento%TYPE,
    pk_carrerain IN Alumno.pk_carrera%TYPE)
AS
BEGIN
    UPDATE Alumno
    SET cedula = NVL(cedulain, cedula),
        nombre = NVL(nombrein, nombre),
        telefono = NVL(telefonoin, telefono),
        email = NVL(emailin, email),
        fecha_nacimiento = NVL(fecha_nacimientoin, fecha_nacimiento),
        pk_carrera = NVL(pk_carrerain, pk_carrera)
    WHERE id_alumno = id_alumnoin;
    COMMIT;
END;
/

-- Eliminar Alumno
CREATE OR REPLACE PROCEDURE eliminarAlumno(id_alumnoin IN Alumno.id_alumno%TYPE)
AS
BEGIN
    DELETE FROM Alumno WHERE id_alumno = id_alumnoin;
    COMMIT;
END;
/

-- Eliminar Alumno por cédula
CREATE OR REPLACE PROCEDURE eliminarAlumnoPorCedula(
    p_cedula IN Alumno.cedula%TYPE
)
AS
BEGIN
    DELETE FROM Alumno WHERE cedula = p_cedula;
    COMMIT;
END;
/

-- Listar Alumnos
CREATE OR REPLACE FUNCTION listarAlumnos
    RETURN Types.ref_cursor
AS
    alumno_cursor Types.ref_cursor;
BEGIN
    OPEN alumno_cursor FOR
        SELECT id_alumno, cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera FROM Alumno;
    RETURN alumno_cursor;
END;
/

-- Buscar Alumno por cédula
CREATE OR REPLACE FUNCTION buscarAlumnoPorCedula(cedulabuscar IN Alumno.cedula%TYPE)
    RETURN Types.ref_cursor
AS
    alumno_cursor Types.ref_cursor;
BEGIN
    OPEN alumno_cursor FOR
        SELECT id_alumno, cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera FROM Alumno WHERE cedula = cedulabuscar;
    RETURN alumno_cursor;
END;
/

-- Buscar Alumno por nombre
CREATE OR REPLACE FUNCTION buscarAlumnoPorNombre(nombrebuscar IN Alumno.nombre%TYPE)
    RETURN Types.ref_cursor
AS
    alumno_cursor Types.ref_cursor;
BEGIN
    OPEN alumno_cursor FOR
        SELECT id_alumno, cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera FROM Alumno WHERE UPPER(nombre) LIKE UPPER('%' || nombrebuscar || '%');
    RETURN alumno_cursor;
END;
/

-- Buscar Alumno por carrera
CREATE OR REPLACE FUNCTION buscarAlumnosPorCarrera(carrerabuscar IN Alumno.pk_carrera%TYPE)
    RETURN Types.ref_cursor
AS
    alumno_cursor Types.ref_cursor;
BEGIN
    OPEN alumno_cursor FOR
        SELECT id_alumno, cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera FROM Alumno WHERE pk_carrera = carrerabuscar;
    RETURN alumno_cursor;
END;
/

-- Buscar alumnos con oferta en un ciclo
CREATE OR REPLACE FUNCTION buscarAlumnosConOfertaEnCiclo(p_ciclo_id IN Ciclo.id_ciclo%TYPE)
    RETURN Types.ref_cursor
AS
    alumno_cursor Types.ref_cursor;
BEGIN
    OPEN alumno_cursor FOR
        SELECT DISTINCT
            a.id_alumno,
            a.cedula,
            a.nombre,
            a.telefono,
            a.email,
            a.fecha_nacimiento,
            a.pk_carrera
        FROM Alumno a
                 JOIN Carrera_Curso cc ON a.pk_carrera = cc.pk_carrera
        WHERE cc.pk_ciclo = p_ciclo_id;
    RETURN alumno_cursor;
END;
/

------------------------------------------------GRUPOS--------------------------------------------

-- Insertar Grupo
CREATE OR REPLACE PROCEDURE insertarGrupo(
    pk_carrera_curso IN Grupo.pk_carrera_curso%TYPE,
    numero_grupo IN Grupo.numero_grupo%TYPE,
    horario IN Grupo.horario%TYPE,
    pk_profesor IN Grupo.pk_profesor%TYPE)
AS
BEGIN
    INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor)
    VALUES (pk_carrera_curso, numero_grupo, horario, pk_profesor);
    COMMIT;
END;
/

-- Modificar Grupo
CREATE OR REPLACE PROCEDURE modificarGrupo(
    id_grupoin IN Grupo.id_grupo%TYPE,
    pk_carrera_cursoin IN Grupo.pk_carrera_curso%TYPE,
    numero_grupoin IN Grupo.numero_grupo%TYPE,
    horarioin IN Grupo.horario%TYPE,
    pk_profesorin IN Grupo.pk_profesor%TYPE)
AS
BEGIN
    UPDATE Grupo
    SET pk_carrera_curso = NVL(pk_carrera_cursoin, pk_carrera_curso),
        numero_grupo = NVL(numero_grupoin, numero_grupo),
        horario = NVL(horarioin, horario),
        pk_profesor = NVL(pk_profesorin, pk_profesor)
    WHERE id_grupo = id_grupoin;
    COMMIT;
END;
/

-- Eliminar Grupo
CREATE OR REPLACE PROCEDURE eliminarGrupo(id_grupoin IN Grupo.id_grupo%TYPE)
AS
BEGIN
    DELETE FROM Grupo WHERE id_grupo = id_grupoin;
    COMMIT;
END;
/

-- Listar Grupos
CREATE OR REPLACE FUNCTION listarGrupos
    RETURN Types.ref_cursor
AS
    grupo_cursor Types.ref_cursor;
BEGIN
    OPEN grupo_cursor FOR
        SELECT id_grupo, pk_carrera_curso, numero_grupo, horario, pk_profesor FROM Grupo;
    RETURN grupo_cursor;
END;
/

-- Buscar grupos por curso(carrera_curso)
CREATE OR REPLACE FUNCTION buscarGruposPorCarreraCurso(
    p_id_carrera IN Carrera.id_carrera%TYPE,
    p_id_curso IN Curso.id_curso%TYPE
)
    RETURN Types.ref_cursor
AS
    grupos_cursor Types.ref_cursor;
BEGIN
    OPEN grupos_cursor FOR
        SELECT g.id_grupo,
               g.pk_carrera_curso,
               g.numero_grupo,
               g.horario,
               p.id_profesor AS pk_profesor,
               p.nombre AS nombre_profesor
        FROM Grupo g
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
                 JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
        WHERE cc.pk_carrera = p_id_carrera
          AND cc.pk_curso = p_id_curso;
    RETURN grupos_cursor;
END;
/

-- Listar grupos por curso, ciclo y carrera
CREATE OR REPLACE FUNCTION buscarGruposPorCursoCicloCarrera(
    p_id_curso IN Curso.id_curso%TYPE,
    p_id_ciclo IN Ciclo.id_ciclo%TYPE,
    p_id_carrera IN Carrera.id_carrera%TYPE
)
    RETURN Types.ref_cursor
AS
    grupos_cursor Types.ref_cursor;
BEGIN
    OPEN grupos_cursor FOR
        SELECT
            g.id_grupo,
            g.pk_carrera_curso,
            g.numero_grupo,
            g.horario,
            p.id_profesor AS pk_profesor,
            p.nombre AS nombre_profesor
        FROM Grupo g
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
                 JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
        WHERE cc.pk_carrera = p_id_carrera
          AND cc.pk_curso = p_id_curso
          AND cc.pk_ciclo = p_id_ciclo;
    RETURN grupos_cursor;
END;
/

-- Buscar grupos por cédula de profesor en ciclo activo
CREATE OR REPLACE FUNCTION buscarGruposPorProfesorCicloActivo(
    p_cedula_profesor IN Profesor.cedula%TYPE
) RETURN Types.ref_cursor AS
    grupos_cursor Types.ref_cursor;
BEGIN
    OPEN grupos_cursor FOR
        SELECT
            g.id_grupo,
            g.numero_grupo,
            g.horario,
            cu.codigo AS codigo_curso,
            cu.nombre AS nombre_curso,
            ca.codigo AS codigo_carrera,
            ca.nombre AS nombre_carrera,
            ci.anio,
            ci.numero AS numero_ciclo
        FROM Grupo g
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
                 JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
                 JOIN Curso cu ON cc.pk_curso = cu.id_curso
                 JOIN Carrera ca ON cc.pk_carrera = ca.id_carrera
                 JOIN Ciclo ci ON cc.pk_ciclo = ci.id_ciclo
        WHERE p.cedula = p_cedula_profesor
          AND ci.estado = 'Activo';

    RETURN grupos_cursor;
END;
/

-- Buscar grupos por cédula de profesor
CREATE OR REPLACE FUNCTION buscarGruposPorProfesor(
    p_cedula IN Profesor.cedula%TYPE
)
    RETURN Types.ref_cursor
AS
    grupos_cursor Types.ref_cursor;
BEGIN
    OPEN grupos_cursor FOR
        SELECT
            g.id_grupo,
            g.pk_carrera_curso,
            g.numero_grupo,
            g.horario,
            p.id_profesor AS pk_profesor,
            p.nombre AS nombre_profesor
        FROM Grupo g
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
        WHERE p.cedula = p_cedula;
    RETURN grupos_cursor;
END;
/

------------------------------------------------MATRICULAS--------------------------------------------

-- Insertar Matricula
CREATE OR REPLACE PROCEDURE insertarMatricula(
    pk_alumno IN Matricula.pk_alumno%TYPE,
    pk_grupo IN Matricula.pk_grupo%TYPE)
AS
BEGIN
    INSERT INTO Matricula (pk_alumno, pk_grupo)
    VALUES (pk_alumno, pk_grupo);
    COMMIT;
END;
/

-- Modificar Matricula
CREATE OR REPLACE PROCEDURE modificarMatricula(
    id_matriculain IN Matricula.id_matricula%TYPE,
    pk_alumnoin IN Matricula.pk_alumno%TYPE,
    pk_grupoin IN Matricula.pk_grupo%TYPE,
    notain IN Matricula.nota%TYPE)
AS
BEGIN
    UPDATE Matricula
    SET pk_alumno = NVL(pk_alumnoin, pk_alumno),
        pk_grupo = NVL(pk_grupoin, pk_grupo),
        nota = NVL(notain, nota)
    WHERE id_matricula = id_matriculain;
    COMMIT;
END;
/

-- Eliminar Matricula
CREATE OR REPLACE PROCEDURE eliminarMatricula(id_matriculain IN Matricula.id_matricula%TYPE)
AS
BEGIN
    DELETE FROM Matricula WHERE id_matricula = id_matriculain;
    COMMIT;
END;
/

-- Listar Matriculas Por Alumno (cedula)
CREATE OR REPLACE FUNCTION listarMatriculasPorAlumno(
    p_cedula IN Alumno.cedula%TYPE
)
    RETURN SYS_REFCURSOR
AS
    matriculas_cursor SYS_REFCURSOR;
BEGIN
    OPEN matriculas_cursor FOR
        SELECT
            m.id_matricula,
            m.nota,
            g.numero_grupo,
            g.horario,
            c.codigo AS codigo_carrera,
            c.nombre AS nombre_carrera,
            cu.codigo AS codigo_curso,
            cu.nombre AS nombre_curso,
            p.nombre AS nombre_profesor,
            p.cedula AS cedula_profesor
        FROM Matricula m
                 JOIN Alumno a ON m.pk_alumno = a.id_alumno
                 JOIN Grupo g ON m.pk_grupo = g.id_grupo
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
                 JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
                 JOIN Carrera c ON cc.pk_carrera = c.id_carrera
                 JOIN Curso cu ON cc.pk_curso = cu.id_curso
        WHERE a.cedula = p_cedula;

    RETURN matriculas_cursor;
END;
/

-- Listar Matriculas Por Alumno Y Ciclo
CREATE OR REPLACE FUNCTION listarMatriculasPorAlumnoYCiclo(
    p_id_alumno IN Alumno.id_alumno%TYPE,
    p_id_ciclo IN Ciclo.id_ciclo%TYPE
)
    RETURN Types.ref_cursor
AS
    matriculas_cursor Types.ref_cursor;
BEGIN
    OPEN matriculas_cursor FOR
        SELECT
            m.id_matricula,
            m.nota,
            g.numero_grupo,
            g.horario,
            c.codigo AS codigo_carrera,
            c.nombre AS nombre_carrera,
            cu.codigo AS codigo_curso,
            cu.nombre AS nombre_curso,
            p.nombre AS nombre_profesor,
            p.cedula AS cedula_profesor
        FROM Matricula m
                 JOIN Grupo g ON m.pk_grupo = g.id_grupo
                 JOIN Profesor p ON g.pk_profesor = p.id_profesor
                 JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
                 JOIN Carrera c ON cc.pk_carrera = c.id_carrera
                 JOIN Curso cu ON cc.pk_curso = cu.id_curso
                 JOIN Ciclo ci ON cc.pk_ciclo = ci.id_ciclo
        WHERE m.pk_alumno = p_id_alumno
          AND ci.id_ciclo = p_id_ciclo;
    RETURN matriculas_cursor;
END;
/

------------------------------------------------USUARIOS--------------------------------------------

-- Insertar Usuario
CREATE OR REPLACE PROCEDURE insertarUsuario(
    cedula IN Usuario.cedula%TYPE,
    clave IN Usuario.clave%TYPE,
    tipo IN Usuario.tipo%TYPE)
AS
BEGIN
    INSERT INTO Usuario (cedula, clave, tipo)
    VALUES (cedula, clave, tipo);
    COMMIT;
END;
/

-- Modificar Usuario
CREATE OR REPLACE PROCEDURE modificarUsuario(
    id_usuarioin IN Usuario.id_usuario%TYPE,
    cedulain IN Usuario.cedula%TYPE,
    clavein IN Usuario.clave%TYPE,
    tipoin IN Usuario.tipo%TYPE)
AS
BEGIN
    UPDATE Usuario
    SET cedula = NVL(cedulain, cedula),
        clave = NVL(clavein, clave),
        tipo = NVL(tipoin, tipo)
    WHERE id_usuario = id_usuarioin;
    COMMIT;
END;
/

-- Eliminar Usuario
CREATE OR REPLACE PROCEDURE eliminarUsuario(id_usuarioin IN Usuario.id_usuario%TYPE)
AS
BEGIN
    DELETE FROM Usuario WHERE id_usuario = id_usuarioin;
    --COMMIT;
END;
/

-- Listar Usuarios
CREATE OR REPLACE FUNCTION listarUsuarios
    RETURN Types.ref_cursor
AS
    usuario_cursor Types.ref_cursor;
BEGIN
    OPEN usuario_cursor FOR
        SELECT id_usuario, cedula, tipo FROM Usuario;
    RETURN usuario_cursor;
END;
/

-- Consultar Usuario por cédula
CREATE OR REPLACE FUNCTION buscarUsuarioPorCedula(cedulabuscar IN Usuario.cedula%TYPE)
    RETURN Types.ref_cursor
AS
    usuario_cursor Types.ref_cursor;
BEGIN
    OPEN usuario_cursor FOR
        SELECT id_usuario, cedula, tipo FROM Usuario WHERE cedula = cedulabuscar;
    RETURN usuario_cursor;
END;
/

-- Login de un Usuario
CREATE OR REPLACE PROCEDURE loginUsuario(
    cedulain IN Usuario.cedula%TYPE,
    clavein IN Usuario.clave%TYPE,
    usuario_cursor OUT Types.ref_cursor
)
AS
BEGIN
    OPEN usuario_cursor FOR
        SELECT id_usuario, cedula, tipo
        FROM Usuario
        WHERE cedula = cedulain AND clave = clavein;
END;
/

----------------------------------------------------------------------------------------------------------
-- VALIDACIONES Y RESTRICCIONES CON TRIGGERS
----------------------------------------------------------------------------------------------------------

-- No permitir eliminar CARRERA con cursos o alumnos asociados
CREATE OR REPLACE TRIGGER trg_prevent_delete_carrera
    BEFORE DELETE ON Carrera
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Carrera_Curso WHERE pk_carrera = :OLD.id_carrera;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'No se puede eliminar la carrera: tiene cursos asociados.');
    END IF;

    SELECT COUNT(*) INTO v_count FROM Alumno WHERE pk_carrera = :OLD.id_carrera;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'No se puede eliminar la carrera: tiene alumnos inscritos.');
    END IF;
END;
/

-- No permitir eliminar CURSO con asociaciones
CREATE OR REPLACE TRIGGER trg_prevent_delete_curso
    BEFORE DELETE ON Curso
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Carrera_Curso WHERE pk_curso = :OLD.id_curso;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'No se puede eliminar el curso: asociado a una carrera o tiene grupos.');
    END IF;
END;
/

-- No permitir eliminar CICLO con asociaciones
CREATE OR REPLACE TRIGGER trg_prevent_delete_ciclo
    BEFORE DELETE ON Ciclo
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Carrera_Curso WHERE pk_ciclo = :OLD.id_ciclo;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20005, 'No se puede eliminar el ciclo: tiene cursos asociados.');
    END IF;
END;
/

-- No permitir eliminar GRUPO con matrículas
CREATE OR REPLACE TRIGGER trg_prevent_delete_grupo
    BEFORE DELETE ON Grupo
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Matricula WHERE pk_grupo = :OLD.id_grupo;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20006, 'No se puede eliminar el grupo: tiene estudiantes matriculados.');
    END IF;
END;
/

-- No permitir eliminar ALUMNO o PROFESOR con usuario asociado
CREATE OR REPLACE TRIGGER trg_prevent_delete_alumno
    BEFORE DELETE ON Alumno
    FOR EACH ROW
DECLARE
    v_count_usuario NUMBER;
    v_count_matricula NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count_usuario FROM Usuario WHERE cedula = :OLD.cedula AND tipo = 'Alumno';
    SELECT COUNT(*) INTO v_count_matricula FROM Matricula WHERE pk_alumno = :OLD.id_alumno;

    IF v_count_usuario > 0 THEN
        RAISE_APPLICATION_ERROR(-20009, 'No se puede eliminar el alumno: existe un usuario asociado.');
    END IF;
    IF v_count_matricula > 0 THEN
        RAISE_APPLICATION_ERROR(-20011, 'No se puede eliminar el alumno: tiene matrículas asociadas.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_prevent_delete_profesor
    BEFORE DELETE ON Profesor
    FOR EACH ROW
DECLARE
    v_count_usuario NUMBER;
    v_count_grupo NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count_usuario FROM Usuario WHERE cedula = :OLD.cedula AND tipo = 'Profesor';
    SELECT COUNT(*) INTO v_count_grupo FROM Grupo WHERE pk_profesor = :OLD.id_profesor;

    IF v_count_usuario > 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'No se puede eliminar el profesor: existe un usuario asociado.');
    END IF;
    IF v_count_grupo > 0 THEN
        RAISE_APPLICATION_ERROR(-20030, 'No se puede eliminar el profesor: tiene grupos asignados.');
    END IF;
END;
/

-- Validaciones de unicidad y formato lógico
CREATE OR REPLACE TRIGGER trg_validar_ciclo_fecha
    BEFORE INSERT OR UPDATE ON Ciclo
    FOR EACH ROW
BEGIN
    IF :NEW.fecha_inicio IS NULL OR :NEW.fecha_fin IS NULL THEN
        RAISE_APPLICATION_ERROR(-20041, 'Las fechas de inicio y fin no pueden ser nulas.');
    END IF;
    IF :NEW.fecha_inicio >= :NEW.fecha_fin THEN
        RAISE_APPLICATION_ERROR(-20020, 'La fecha de inicio debe ser anterior a la fecha de fin.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_validar_datos_alumno
    BEFORE INSERT OR UPDATE ON Alumno
    FOR EACH ROW
BEGIN
    IF :NEW.nombre IS NULL OR TRIM(:NEW.nombre) = '' THEN
        RAISE_APPLICATION_ERROR(-20021, 'El nombre del alumno no puede estar vacío.');
    END IF;

    IF :NEW.email IS NULL OR NOT REGEXP_LIKE(:NEW.email, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$') THEN
        RAISE_APPLICATION_ERROR(-20022, 'El correo del alumno no tiene un formato válido.');
    END IF;

    IF :NEW.fecha_nacimiento IS NULL OR :NEW.fecha_nacimiento > SYSDATE THEN
        RAISE_APPLICATION_ERROR(-20023, 'La fecha de nacimiento debe ser válida y no futura.');
    END IF;

    IF :NEW.cedula IS NULL OR NOT REGEXP_LIKE(:NEW.cedula, '^[0-9]{9}$') THEN
        RAISE_APPLICATION_ERROR(-20036, 'La cédula del alumno debe ser de 9 dígitos numéricos.');
    END IF;

    IF :NEW.telefono IS NULL OR NOT REGEXP_LIKE(:NEW.telefono, '^[0-9]{8}$') THEN
        RAISE_APPLICATION_ERROR(-20037, 'El teléfono debe ser de 8 dígitos numéricos.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_validar_datos_profesor
    BEFORE INSERT OR UPDATE ON Profesor
    FOR EACH ROW
BEGIN
    IF :NEW.nombre IS NULL OR TRIM(:NEW.nombre) = '' THEN
        RAISE_APPLICATION_ERROR(-20024, 'El nombre del profesor no puede estar vacío.');
    END IF;

    IF :NEW.email IS NULL OR NOT REGEXP_LIKE(:NEW.email, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$') THEN
        RAISE_APPLICATION_ERROR(-20025, 'El correo del profesor no tiene un formato válido.');
    END IF;

    IF :NEW.cedula IS NULL OR NOT REGEXP_LIKE(:NEW.cedula, '^[0-9]{9}$') THEN
        RAISE_APPLICATION_ERROR(-20038, 'La cédula del profesor debe ser de 9 dígitos numéricos.');
    END IF;

    IF :NEW.telefono IS NULL OR NOT REGEXP_LIKE(:NEW.telefono, '^[0-9]{8}$') THEN
        RAISE_APPLICATION_ERROR(-20039, 'El teléfono del profesor debe ser de 8 dígitos numéricos.');
    END IF;
END;
/

-- Validar duplicado de curso en carrera y ciclo
CREATE OR REPLACE TRIGGER trg_prevent_duplicate_carrera_curso
    BEFORE INSERT ON Carrera_Curso
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Carrera_Curso
    WHERE pk_carrera = :NEW.pk_carrera AND pk_curso = :NEW.pk_curso AND pk_ciclo = :NEW.pk_ciclo;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20026, 'Ya existe una asociación de ese curso en esa carrera y ciclo.');
    END IF;
END;
/

-- Validar duplicado de grupo dentro del mismo curso y ciclo
CREATE OR REPLACE TRIGGER trg_prevent_duplicate_grupo
    BEFORE INSERT ON Grupo
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Grupo g
                                          JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
    WHERE cc.pk_curso = (SELECT pk_curso FROM Carrera_Curso WHERE id_carrera_curso = :NEW.pk_carrera_curso)
      AND cc.pk_ciclo = (SELECT pk_ciclo FROM Carrera_Curso WHERE id_carrera_curso = :NEW.pk_carrera_curso)
      AND g.numero_grupo = :NEW.numero_grupo;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20027, 'Ya existe un grupo con ese número para el mismo curso y ciclo.');
    END IF;
END;
/

-- Validar duplicado de usuario por cédula
CREATE OR REPLACE TRIGGER trg_prevent_duplicate_usuario
    BEFORE INSERT ON Usuario
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Usuario WHERE cedula = :NEW.cedula;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20028, 'Ya existe un usuario con esta cédula.');
    END IF;
END;
/

-- Validar duplicado de matrícula por alumno y curso
CREATE OR REPLACE TRIGGER trg_prevent_duplicate_matricula
    BEFORE INSERT ON Matricula
    FOR EACH ROW
DECLARE
    v_id_curso NUMBER;
    v_count NUMBER;
BEGIN
    SELECT cc.pk_curso INTO v_id_curso
    FROM Grupo g JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
    WHERE g.id_grupo = :NEW.pk_grupo;

    SELECT COUNT(*) INTO v_count
    FROM Matricula m
             JOIN Grupo g ON m.pk_grupo = g.id_grupo
             JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
    WHERE m.pk_alumno = :NEW.pk_alumno AND cc.pk_curso = v_id_curso;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20029, 'El alumno ya está matriculado en otro grupo de este curso.');
    END IF;
END;
/

-- Restringe la matrícula a cursos de la carrera del alumno
CREATE OR REPLACE TRIGGER trg_restrict_matricula_carrera
    BEFORE INSERT ON Matricula
    FOR EACH ROW
DECLARE
    v_alumno_carrera NUMBER;
    v_curso_carrera NUMBER;
BEGIN
    SELECT pk_carrera INTO v_alumno_carrera
    FROM Alumno
    WHERE id_alumno = :NEW.pk_alumno;

    SELECT cc.pk_carrera INTO v_curso_carrera
    FROM Grupo g
             JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso
    WHERE g.id_grupo = :NEW.pk_grupo;

    IF v_alumno_carrera != v_curso_carrera THEN
        RAISE_APPLICATION_ERROR(-20040, 'El alumno solo puede matricular cursos de su carrera.');
    END IF;
END;
/

-- Valida que la nota en Matricula esté entre 0 y 100
CREATE OR REPLACE TRIGGER trg_validate_matricula_nota
    BEFORE INSERT OR UPDATE ON Matricula
    FOR EACH ROW
BEGIN
    IF :NEW.nota IS NOT NULL AND (:NEW.nota < 0 OR :NEW.nota > 100) THEN
        RAISE_APPLICATION_ERROR(-20031, 'La nota debe estar entre 0 y 100.');
    END IF;
END;
/

/*-- Asegura que solo un Ciclo esté Activo
CREATE OR REPLACE TRIGGER trg_ensure_single_active_ciclo
    BEFORE INSERT OR UPDATE OF estado ON Ciclo
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    IF :NEW.estado = 'Activo' THEN
        SELECT COUNT(*) INTO v_count FROM Ciclo
        WHERE estado = 'Activo' AND id_ciclo != NVL(:OLD.id_ciclo, -1);
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20032, 'Ya existe un ciclo activo. Desactive el ciclo actual primero.');
        END IF;
    END IF;
END;
/

-- Valida que Usuario con tipo Alumno o Profesor tenga registro correspondiente
CREATE OR REPLACE TRIGGER trg_validate_usuario
    BEFORE INSERT OR UPDATE OF tipo ON Usuario
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    IF :NEW.tipo = 'Alumno' THEN
        SELECT COUNT(*) INTO v_count FROM Alumno WHERE cedula = :NEW.cedula;
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20033, 'No existe un alumno con esta cédula para asignar como usuario.');
        END IF;
    ELSIF :NEW.tipo = 'Profesor' THEN
        SELECT COUNT(*) INTO v_count FROM Profesor WHERE cedula = :NEW.cedula;
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20034, 'No existe un profesor con esta cédula para asignar como usuario.');
        END IF;
    END IF;
END;
/*/

-- Evita la eliminación de Carrera_Curso con grupos asociados
CREATE OR REPLACE TRIGGER trg_prevent_delete_carrera_curso
    BEFORE DELETE ON Carrera_Curso
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM Grupo WHERE pk_carrera_curso = :OLD.id_carrera_curso;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20035, 'No se puede eliminar la relación carrera-curso: tiene grupos asociados.');
    END IF;
END;
/


----------------------------------------------------------------------------------------------------------
-- INSERCIÓN DE DATOS DE PRUEBA
----------------------------------------------------------------------------------------------------------

-- CARRERAS
INSERT INTO Carrera (codigo, nombre, titulo) VALUES ('INF01', 'Ingeniería en Informática', 'Bachiller en Ingeniería en Informática');
INSERT INTO Carrera (codigo, nombre, titulo) VALUES ('ADM01', 'Administración de Empresas', 'Bachiller en Administración');
INSERT INTO Carrera (codigo, nombre, titulo) VALUES ('MAT01', 'Matemáticas Aplicadas', 'Bachiller en Matemáticas Aplicadas');

-- CURSOS
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('INF101', 'Programación I', 4, 6);
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('INF201', 'Estructuras de Datos', 4, 5);
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('ADM101', 'Contabilidad Básica', 3, 4);
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('ADM201', 'Gestión Financiera', 4, 5);
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('MAT101', 'Cálculo I', 4, 6);
INSERT INTO Curso (codigo, nombre, creditos, horas_semanales) VALUES ('MAT201', 'Álgebra Lineal', 4, 5);

-- CICLOS
INSERT INTO Ciclo (anio, numero, fecha_inicio, fecha_fin, estado) VALUES (2025, 1, TO_DATE('2025-02-01', 'YYYY-MM-DD'), TO_DATE('2025-06-01', 'YYYY-MM-DD'), 'Activo');
INSERT INTO Ciclo (anio, numero, fecha_inicio, fecha_fin, estado) VALUES (2025, 2, TO_DATE('2025-07-01', 'YYYY-MM-DD'), TO_DATE('2025-11-01', 'YYYY-MM-DD'), 'Inactivo');

-- CARRERA_CURSO
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (1, 1, 1); -- INF01, Programación I, Ciclo 1
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (1, 2, 1); -- INF01, Estructuras de Datos, Ciclo 1
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (2, 3, 1); -- ADM01, Contabilidad Básica, Ciclo 1
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (2, 4, 1); -- ADM01, Gestión Financiera, Ciclo 1
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (3, 5, 1); -- MAT01, Cálculo I, Ciclo 1
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo) VALUES (3, 6, 1); -- MAT01, Álgebra Lineal, Ciclo 1

-- PROFESORES
INSERT INTO Profesor (cedula, nombre, telefono, email) VALUES ('100100100', 'Carlos Rojas', '88888888', 'crojas@una.ac.cr');
INSERT INTO Profesor (cedula, nombre, telefono, email) VALUES ('200200200', 'María López', '87777777', 'mlopez@una.ac.cr');
INSERT INTO Profesor (cedula, nombre, telefono, email) VALUES ('300300300', 'Luis Jiménez', '86666666', 'ljimenez@una.ac.cr');
INSERT INTO Profesor (cedula, nombre, telefono, email) VALUES ('400400400', 'Ana Salas', '85555555', 'asalas@una.ac.cr');

-- ALUMNOS
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('111111111', 'Laura Fernández', '85001234', 'laura@estudiante.una.ac.cr', TO_DATE('2000-03-15', 'YYYY-MM-DD'), 1);
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('222222222', 'José Ramírez', '84005678', 'jose@estudiante.una.ac.cr', TO_DATE('1999-11-20', 'YYYY-MM-DD'), 1);
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('333333333', 'Ana Rodríguez', '87007890', 'ana@estudiante.una.ac.cr', TO_DATE('2001-06-10', 'YYYY-MM-DD'), 2);
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('444444444', 'David Castro', '83009876', 'david@estudiante.una.ac.cr', TO_DATE('2000-12-25', 'YYYY-MM-DD'), 2);
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('555555555', 'Carla Sánchez', '81001234', 'carla@estudiante.una.ac.cr', TO_DATE('2001-05-12', 'YYYY-MM-DD'), 3);
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera) VALUES ('666666666', 'Miguel Torres', '82005678', 'miguel@estudiante.una.ac.cr', TO_DATE('2000-09-25', 'YYYY-MM-DD'), 3);

-- GRUPOS
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (1, 1, 'Lunes 8:00-10:00', 1); -- Programación I, Grupo 1, Carlos Rojas
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (1, 2, 'Martes 10:00-12:00', 2); -- Programación I, Grupo 2, María López
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (3, 1, 'Miércoles 9:00-11:00', 3); -- Contabilidad Básica, Grupo 1, Luis Jiménez
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (4, 1, 'Jueves 14:00-16:00', 3); -- Gestión Financiera, Grupo 1, Luis Jiménez
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (5, 1, 'Viernes 8:00-10:00', 4); -- Cálculo I, Grupo 1, Ana Salas
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor) VALUES (6, 1, 'Lunes 10:00-12:00', 4); -- Álgebra Lineal, Grupo 1, Ana Salas

-- MATRICULAS
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (1, 1, 85); -- Laura Fernández, Programación I, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (2, 1, 78); -- José Ramírez, Programación I, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (1, 2, 90); -- Laura Fernández, Programación I, Grupo 2
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (3, 3, 82); -- Ana Rodríguez, Contabilidad Básica, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (4, 3, 88); -- David Castro, Contabilidad Básica, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (3, 4, 75); -- Ana Rodríguez, Gestión Financiera, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (5, 5, 92); -- Carla Sánchez, Cálculo I, Grupo 1
INSERT INTO Matricula (pk_alumno, pk_grupo, nota) VALUES (6, 5, 80); -- Miguel Torres, Cálculo I, Grupo 1

-- USUARIOS
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('100100100', 'prof123', 'Profesor'); -- Carlos Rojas
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('200200200', 'prof456', 'Profesor'); -- María López
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('300300300', 'prof789', 'Profesor'); -- Luis Jiménez
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('111111111', 'alumno1', 'Alumno'); -- Laura Fernández
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('333333333', 'alumno2', 'Alumno'); -- Ana Rodríguez
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('555555555', 'alumno3', 'Alumno'); -- Carla Sánchez
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('500500500', 'admin123', 'Administrador'); -- Administrador
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('600600600', 'matri123', 'Matriculador'); -- Matriculador

COMMIT;

----------------------------------------------------------------------------------------------------------
-- PRUEBAS
----------------------------------------------------------------------------------------------------------

-- Visualización general
SELECT * FROM CARRERA;
SELECT * FROM CURSO;
SELECT * FROM CICLO;
SELECT * FROM CARRERA_CURSO;
SELECT * FROM PROFESOR;
SELECT * FROM ALUMNO;
SELECT * FROM GRUPO;
SELECT * FROM MATRICULA;
SELECT * FROM USUARIO;

-- Ver procedimientos y funciones compilados
SELECT OBJECT_NAME, PROCEDURE_NAME, OBJECT_TYPE
FROM USER_PROCEDURES
WHERE OBJECT_TYPE IN ('FUNCTION', 'PROCEDURE')
ORDER BY OBJECT_NAME, PROCEDURE_NAME;

-- Ver triggers creados por el usuario
SELECT TRIGGER_NAME, TABLE_NAME, TRIGGER_TYPE, STATUS
FROM USER_TRIGGERS
ORDER BY TABLE_NAME, TRIGGER_NAME;

-- Script de pruebas para validación de restricciones en triggers
/*
-- 1. Prueba: eliminar carrera con cursos (ID 1)
BEGIN
DELETE FROM Carrera WHERE id_carrera = 1;
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - No se puede eliminar carrera con cursos: ' || SQLERRM);
END;
/

-- 2. Prueba: insertar alumno con fecha de nacimiento futura
BEGIN
INSERT INTO Alumno (cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera)
VALUES ('999000999', 'Error Test', '80000000', 'error@correo.com', SYSDATE + 10, 1);
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - Fecha nacimiento futura inválida: ' || SQLERRM);
END;
/

-- 3. Prueba: insertar usuario duplicado (cédula ya existe)
BEGIN
INSERT INTO Usuario (cedula, clave, tipo) VALUES ('111111111', 'clave123', 'Alumno');
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - Usuario duplicado por cédula: ' || SQLERRM);
END;
/

-- 4. Prueba: insertar matrícula duplicada (mismo alumno, mismo curso diferente grupo)
-- Alumno 1 ya matriculado en curso INF101 (grupo 1 y 2)
BEGIN
INSERT INTO Matricula (pk_alumno, pk_grupo) VALUES (1, 2);
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - Matrícula duplicada en curso: ' || SQLERRM);
END;
/

-- 5. Prueba: insertar grupo duplicado (mismo número para curso y ciclo)
BEGIN
INSERT INTO Grupo (pk_carrera_curso, numero_grupo, horario, pk_profesor)
VALUES (1, 1, 'Viernes 13:00-15:00', 2);
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - Grupo duplicado detectado: ' || SQLERRM);
END;
/

-- 6. Prueba: insertar curso en carrera duplicado
BEGIN
INSERT INTO Carrera_Curso (pk_carrera, pk_curso, pk_ciclo)
VALUES (1, 1, 1);
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - Curso duplicado en carrera/ciclo: ' || SQLERRM);
END;
/

-- 7. Prueba: eliminar alumno con usuario asociado (cedula 111111111)
BEGIN
DELETE FROM Alumno WHERE cedula = '111111111';
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - No se puede eliminar alumno con usuario: ' || SQLERRM);
END;
/

-- 8. Prueba: eliminar profesor con usuario asociado (cedula 100100100)
BEGIN
DELETE FROM Profesor WHERE cedula = '100100100';
EXCEPTION
  WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('OK - No se puede eliminar profesor con usuario: ' || SQLERRM);
END;
/
*/
