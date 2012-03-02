f = open('movies.json', 'r+')
txt = f.read()
f.close()

# Keep on replacing values in the document until it works
can = txt.replace("actor:'","actor:['")
while can != -1:
  txt = txt.replace("actor:'","actor:['")
  can = txt.replace("actor:'","actor:['")

f = open('movies.json', 'w')
f.write(txt)
f.close()
