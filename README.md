# Elasticsearch as a service on top of Apache Mesos and DC/OS

This is designed for a series of conference talks. You can find anything you need to know about DC/OS or this demo in this document or the outgoing links. This is a rather complex demo, so grab your favorit beer and enjoy 🍺

## Abstract
You are a bigger company and want to offer a self service solution for your employees to start and stop data stores on demand? Or are deploying highly dynamically applications and want the freedom to orchestrate your data services like Elasticsearch besides the rest of your architecture to profit from scaling effects? Especially talking about cluster resource utilization, various workloads or failures are challenges for the modern datacenter. To minimize these problems and maximize your cluster utilization, Apache Mesos and DC/OS, the datacenter operating system, helps you to operate huge clusters and operate your service architecture.
In this session we will see the motivation why big users operate Apache Mesos and how easy it is to implement a Elasticsearch as a service solution on top of it.


## Prerequisits
If you are totally new to [DC/OS](https://dcos.io/) or container orchestration in general, I would recommend you to visit our website and check the [documentation](https://dcos.io/docs/1.10/) section.

If you don't want to read documentation and a more entertaining (Monkey Island™) introduction, I am inviting you to watch this youbube video 👇❤️

[![Youtube talk](images/youtube.png)](https://www.youtube.com/watch?v=u2mpN2GxfVY)


## Get this demo working on DC/OS
- You need a running DC/OS cluster to run this demo
- You have multiple options to spin up a DC/OS cluster
	- Go to https://dcos.io/ and to walk through the possibilities or browse the docs
	- Go to https://github.com/dcos/dcos-vagrant if you want to play around on your local box

Please check this page 👇
[![Youtube talk](images/install.png)](https://dcos.io/install/)


### 1. Install management application

Simply run the following command to deploy our management application on a running DC/OS cluster:

```
dcos marathon group add web-app/marathon-configuration.json
```

TBD