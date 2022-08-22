# ----- You can find the new timestamped tags here: https://hub.docker.com/r/gitpod/workspace-full/tags -----
# FROM gitpod/workspace-full:[tag]
FROM gitpod/workspace-full

# ----- Install custom tools, runtime, etc. -----

# yes command is for answering 'setting this java version as default'
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && yes | sdk install java 17.0.3-ms"