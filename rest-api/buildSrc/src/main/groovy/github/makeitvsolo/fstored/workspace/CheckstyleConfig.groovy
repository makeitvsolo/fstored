package github.makeitvsolo.fstored.workspace


import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle

class CheckstyleConfig {

    static void useCheckstyle(Project self) {
        self.apply plugin: 'checkstyle'

        self.checkstyle {
            toolVersion = '10.3.3'
            configFile = self.rootProject.file('checkstyle.xml')
            sourceSets = [sourceSets.main]
            showViolations = true
            ignoreFailures = false
        }

        self.checkstyleMain {

        }

        self.checkstyleTest {
            enabled = false
        }

        self.tasks.withType(Checkstyle).configureEach {
            reports {
                html.destination self.rootProject.file('build/reports/checkstyle.html')
            }
        }
    }
}
