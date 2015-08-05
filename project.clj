(defproject score_card "0.1.0"
  :description "Golf score sharing tool. Web app."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [org.clojure/java.jdbc "0.2.1"]
                 [ring/ring-jetty-adapter "1.0.1"]
                 [compojure "1.0.1"]
                 [hiccup "0.3.8"]
                 [sandbar/sandbar "0.4.0-SNAPSHOT"]]
  :dev-dependencies [[clj-stacktrace "0.2.4"]
                     [ring-serve "0.1.2"]]
  :web-content "public"
  :repositories  {"sonatype-oss-public"
                  "https://oss.sonatype.org/content/groups/public/"})