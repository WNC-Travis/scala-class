package guice

import com.google.inject.{ AbstractModule, Guice, Inject, Stage }
import guice.AdHocExample.{ ProgramController, SponsorController }
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule

trait Routes {
  def + : Routes
}

object NullRoutes extends Routes {
  override def + : Routes = NullRoutes
}

object AdHocExample extends App {
  class ModelDao()
  class ModelService(modelDao: ModelDao)

  class ProgramService(modelService: ModelService, modelDao: ModelDao) {
    def run = println("Initialized")
  }

  class ProgramController(programService: ProgramService) {
    def routes: Routes = NullRoutes
  }

  class ProgramModule {
    lazy val dao = new ModelDao()
    lazy val modelService = new ModelService(dao)
    lazy val programService = new ProgramService(modelService, dao)
    lazy val programController = new ProgramController(programService)
  }

  class SponsorService()
  class SponsorPlanService(sponsorService: SponsorService, programService: ProgramService)

  class SponsorController(sponsorPlanService: SponsorPlanService) {
    def routes: Routes = NullRoutes
  }

  class SponsorModule {
    lazy val sponsor = new SponsorService
    lazy val sponsorPlan = new SponsorPlanService(sponsor, new ProgramModule().programService)
    lazy val controller = new SponsorController(sponsorPlan)
  }

  (new ProgramModule).programService.run

  class AllRoutes(sponsorController: SponsorController, programController: ProgramController) {
    def routes: Routes = sponsorController.routes + programController.routes
  }

  class AllRoutesModule {
    lazy val allRoutes = new AllRoutes(new SponsorModule().controller, new ProgramModule().programController)
  }

  trait AdvisorService {}

}

object GuiceExample extends App {

  @javax.inject.Singleton
  class ModelDao() {}

  @javax.inject.Singleton
  class ModelService @Inject()(modelDao: ModelDao) {}

  @javax.inject.Singleton
  class ProgramService @Inject()(modelService: ModelService, modelDao: ModelDao) {
    def run = println("Initialized")
  }

  @javax.inject.Singleton
  class SponsorService()

  @javax.inject.Singleton
  class SponsorPlanService @Inject()(sponsorService: SponsorService, programService: ProgramService)

  @javax.inject.Singleton
  class AllRoutes @Inject()(sponsorController: SponsorController, programController: ProgramController) {
    def routes: Routes = sponsorController.routes + programController.routes
  }

  object Boot extends App {

    class GuiceConfig extends AbstractModule with ScalaModule {
      // various non-default binds.
    }
    val guiceInjector: ScalaInjector = new ScalaInjector(Guice.createInjector(Stage.PRODUCTION, new GuiceConfig))

    guiceInjector.instance[AllRoutes]

  }

}
