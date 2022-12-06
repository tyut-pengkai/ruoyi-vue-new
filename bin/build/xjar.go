package main

import (
	"bytes"
	"errors"
	"os"
	"os/exec"
	"strings"
)

var xKey = XKey{
	algorithm: []byte{65, 69, 83, 47, 67, 66, 67, 47, 80, 75, 67, 83, 53, 80, 97, 100, 100, 105, 110, 103},
	keysize:   []byte{49, 50, 56},
	ivsize:    []byte{49, 50, 56},
	password:  []byte{104, 121, 46, 99, 111, 111, 114, 100, 115, 111, 102, 116, 46, 99, 111, 109, 64, 50, 48, 50, 50, 48, 53, 48, 49, 35, 122, 119, 103, 117},
}

func main() {

	// check agent forbid
	{
		args := os.Args
		l := len(args)
		for i := 0; i < l; i++ {
			arg := args[i]
			if strings.HasPrefix(arg, "-javaagent:") {
				panic(errors.New("agent forbidden"))
			}
		}
	}

	// start java application
	java := os.Args[1]
	args := os.Args[2:]
	key := bytes.Join([][]byte{
		xKey.algorithm, {13, 10},
		xKey.keysize, {13, 10},
		xKey.ivsize, {13, 10},
		xKey.password, {13, 10},
	}, []byte{})
	cmd := exec.Command(java, args...)
	cmd.Stdin = bytes.NewReader(key)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		panic(err)
	}
}

// find jar name from args
func JAR(args []string) (string, error) {
	var jar string

	l := len(args)
	for i := 1; i < l-1; i++ {
		arg := args[i]
		if arg == "-jar" {
			jar = args[i+1]
		}
	}

	if jar == "" {
		return "", errors.New("unspecified jar name")
	}

	return jar, nil
}

type XKey struct {
	algorithm []byte
	keysize   []byte
	ivsize    []byte
	password  []byte
}
