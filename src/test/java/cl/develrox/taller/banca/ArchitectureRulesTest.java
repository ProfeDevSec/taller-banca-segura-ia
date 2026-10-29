package cl.develrox.taller.banca;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Security as Code: reglas de /security que se verifican automaticamente en cada build.
 * Una instruccion al agente puede ignorarse; una prueba que falla, no.
 */
@ArchTag("security")
@AnalyzeClasses(packages = "cl.develrox.taller.banca", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureRulesTest {

    @ArchTest
    static final ArchRule controladoresNoExponenEntidades = noClasses()
        .that().areAnnotatedWith(RestController.class)
        .should().dependOnClassesThat().areAnnotatedWith(Entity.class)
        .because("SEC-API-03: las entidades de persistencia no son contratos de API (usar DTO + Mapper)");

    @ArchTest
    static final ArchRule dtosNoDependenDeEntidades = noClasses()
        .that().resideInAPackage("..dto..")
        .should().dependOnClassesThat().areAnnotatedWith(Entity.class)
        .because("SEC-API-03: un DTO no debe envolver ni exponer una entidad");

    @ArchTest
    static final ArchRule controladoresNoAccedenARepositorios = noClasses()
        .that().areAnnotatedWith(RestController.class)
        .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
        .because("Arquitectura definida: Controller -> Application Service -> Domain -> Repository");
}
