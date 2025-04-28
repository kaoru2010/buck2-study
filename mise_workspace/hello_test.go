package main

import "testing"

func TestMessage(t *testing.T) {
	expected := "Hello, World!"
	actual := Message()

	if actual != expected {
		t.Errorf("Message() = %q; want %q", actual, expected)
	}
}