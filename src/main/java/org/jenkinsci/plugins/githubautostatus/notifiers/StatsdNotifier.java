
package org.jenkinsci.plugins.githubautostatus.notifiers;

import org.jenkinsci.plugins.githubautostatus.StatsdWrapper;
import org.jenkinsci.plugins.githubautostatus.StatsdNotifierConfig;

import java.util.logging.Logger;

public class StatsdNotifier implements BuildNotifier {
    private StatsdWrapper client;
    protected StatsdNotifierConfig config;

    public StatsdNotifier(StatsdNotifierConfig cfg) {
        config = cfg;
        // TODO: Fix the hostname configuration
        // TODO: Fix the hostname configuration
        // TODO: Fix the hostname configuration
        // TODO: Fix the hostname configuration
        // TODO: Fix the hostname configuration
        //client = new StatsdWrapper(cfg.getStatsdBucket(), cfg.getStatsdHost(), Integer.parseInt(cfg.getStatsdPort()));
        client = new StatsdWrapper(cfg.getStatsdBucket(), cfg.getStatsdHost(), Integer.parseInt(cfg.getStatsdPort()));
    }

    public boolean isEnabled() {
        return client != null;
    }


    private String getBranchPath() {
        return String.format("pipeline.%s.%s.branch.%s", config.getRepoOwner(), config.getRepoName(), config.getBranchName());
    }
    //service.jenkins-kubernetes.pipeline.<folder>.<subfolder>.<job name>.branch.<branch>.stage.<stage name>.duration (Timer metric)
    public void notifyBuildState(String jobName, String nodeName, BuildState buildState) {
        LOGGER.info("[YO] Sending notify build state!");
        String fqp = String.format("%s.stage.%s.status.%s", getBranchPath(), nodeName, buildState);  
        client.increment(fqp, 1);
    }

    public void notifyBuildStageStatus(String jobName, String nodeName, BuildState buildState, long nodeDuration) {
        LOGGER.info("[YO] sending build stage status!");
        String fqp = String.format("%s.stage.%s.duration", getBranchPath(), nodeName);
        client.time(fqp, nodeDuration);
    }

    // 1 & 2.
    public void notifyFinalBuildStatus(String jobName, BuildState buildState, long buildDuration, long blockedDuration) {
        LOGGER.info("[YO] notifying final build stats !!!!");
        String fqp = String.format("%s.job.status.%s", getBranchPath(), buildState);
        client.increment(fqp, 1);
        fqp = String.format("%s.job.duration", getBranchPath());
        client.time(fqp, buildDuration);
        fqp = String.format("%s.job.blocked_duration", getBranchPath());
        client.time(fqp, buildDuration);
    }

    private static final Logger LOGGER = Logger.getLogger(StatsdWrapper.class.getName());
    public void sendNonStageError(String jobName, String nodeName) {
        LOGGER.info("TODO: implement sendNonStageError method");
    }
}