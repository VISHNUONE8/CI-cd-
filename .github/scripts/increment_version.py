# .github/scripts/increment_version.py
import re
import sys
import os

def increment_version_code(file_path, file_type):
    if file_type == 'kts':
        # Regex for: versionCode = 123 or versionCode = 123 // some comment
        # Allows for variable spacing around '='
        pattern = re.compile(r"(\bversionCode\s*=\s*)(\d+)")
    elif file_type == 'groovy':
        # Regex for: versionCode 123 or versionCode 123 // some comment
        # Allows for variable spacing after 'versionCode'
        pattern = re.compile(r"(\bversionCode\s+)(\d+)")
    else:
        print(f"Unsupported file type: {file_type}", file=sys.stderr)
        sys.exit(1)

    new_lines = []
    version_code_updated = False
    new_version_code_val = -1

    try:
        with open(file_path, 'r') as f:
            for line in f:
                if not version_code_updated: # Process only the first match
                    match = pattern.search(line)
                    if match:
                        current_version_code = int(match.group(2))
                        new_version_code_val = current_version_code + 1
                        # match.group(1) is the part like "versionCode = " or "versionCode  "
                        new_line = pattern.sub(r"\g<1>" + str(new_version_code_val), line, 1)
                        new_lines.append(new_line)
                        version_code_updated = True
                        print(f"Found and updated versionCode from {current_version_code} to {new_version_code_val} in {file_path}")
                        continue
                new_lines.append(line)

        if version_code_updated:
            with open(file_path, 'w') as f:
                f.writelines(new_lines)
            # Output for GitHub Actions
            if 'GITHUB_OUTPUT' in os.environ:
                with open(os.environ['GITHUB_OUTPUT'], 'a') as hf:
                    print(f"new_version_code={new_version_code_val}", file=hf)
            else:
                print(f"::set-output name=new_version_code::{new_version_code_val}") # Fallback for older runners

        else:
            print(f"versionCode not found or not updated in {file_path}.", file=sys.stderr)
            sys.exit(1)

    except FileNotFoundError:
        print(f"Error: File not found at {file_path}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"An error occurred: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python increment_version.py <path_to_gradle_file> <kts|groovy>", file=sys.stderr)
        sys.exit(1)

    file_path_arg = sys.argv[1]
    file_type_arg = sys.argv[2].lower()
    increment_version_code(file_path_arg, file_type_arg)