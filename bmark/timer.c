/*
 *  timer.c -- time the execution of a command
 *
 *  usage:  timer.c command [arg ...]
 *
 *  Runs "command" in a shell and writes user time, system time, and
 *  total of those two to standard error.  The command's standard error
 *  is merged into its standard output.
 *
 *  This little C program is used avoid dealing with the
 *  incompatibilities of the various shell timing commands.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/times.h>

int main (int argc, char *argv[]) {
    int i;
    int n = 0;
    char *cmd;
    struct tms t1, t2;
    double usr, sys;

    argv[0] = "2>&1";			/* redirects stderr onto stdout */
    for (i = 0; i < argc; i++) {
    	n += strlen(argv[i] + 1);	/* total lengths of command words */
    }
    cmd = malloc(n + 1);		/* alloc command buffer */
    for (i = 0; i < argc; i++) {
    	strcat(cmd, argv[i]);		/* construct command string */
	strcat(cmd, " ");
    }

    times(&t1);				/* record CPU usage before */
    system(cmd);			/* run command */
    times(&t2);				/* record CPU usage after */

    usr = (t2.tms_cutime - t1.tms_cutime) / (double) CLK_TCK;
    sys = (t2.tms_cstime - t1.tms_cstime) / (double) CLK_TCK;
    fprintf(stderr, "%8.3f %8.3f %8.3f\n", usr, sys, usr + sys);
    return 0;
}
