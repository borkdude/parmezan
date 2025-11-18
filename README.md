# Parmezan

Parmezan fixes unbalanced or unexpected parens or other delimiters in Clojure files.

## API

The main API function is `borkdude.parmezan/parmezan` which takes a string of Clojure and returns a fixed string of Clojure.

## Command line

Options:

```
  --file  The file you want to fix
  --write Modify file in place
```

The CLI `-main` function is `borkdude.parmezan.cli/-main`.
The CLI exec function is `borkdude.parmezan.cli/exec`.

## Install

For babashka, you can install this tool with [bbin](https://github.com/babashka/bbin):

```
bbin install io.github.borkdude/parmezan
```

## Emacs integration

Once installed with bbin, you can hook this tool up with Emacs like this:

```
(defun parmezan ()
  "Run parmezan on the current buffer."
  (interactive)
  (when (buffer-file-name)
    (save-buffer)
    (shell-command (format "parmezan --file %s --write"
                          (shell-quote-argument (buffer-file-name))))
    (revert-buffer t t t)))
```

In a file with unbalanced delimiters, you then call `M-x parmezan` to fix it.

## License

MIT
