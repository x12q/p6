# This is a python script to generate proto definition for python.
# Thi output is in "python_out" folder.
import os
from grpc_tools import protoc

path = "./src/main/proto"
fileList = []
for root, dirs, files in os.walk(path):
    for file in files:
        # append the file name to the list
        fileList.append(os.path.join(root, file))

protoFiles = fileList
outputFolder = "./python_out"
if not os.path.exists(outputFolder):
    os.mkdir(outputFolder)
for file in protoFiles:
    protoc.main([
        'grpc_tools.protoc',
        f'--proto_path={path}',
        f'--python_out={outputFolder}',
        f'--grpc_python_out={outputFolder}',
        file
    ])
