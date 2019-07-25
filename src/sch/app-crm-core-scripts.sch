;; Place scripts that we want to make available for all CRM applications in this file

(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.core.bean.ScriptHome.class) (let ((b (com.redknee.framework.core.bean.Script.))) (setBeanProperty b "Name" "ConfigShare.RuleAudit")(setBeanProperty b "Script" "invalid = new java.util.ArrayList();
defaults = new java.util.ArrayList();
valid = new java.util.HashMap();
for (Object obj : ctx.get(com.redknee.app.crm.configshare.SharedBeanHome.class).selectAll(ctx))
{
request = new com.redknee.app.crm.configshare.ConfigChangeRequest();
request.setBeanClass(obj.getBeanClassName());
match = com.redknee.util.partitioning.xhome.visitor.RuleMatchingVisitor.RuleMatchingHelper.getBestMatch(ctx, request);
if (match == null)
{
invalid.add(obj.getBeanClassName());
}
else if (match.getName().equals(\"Default (All Nodes)\"))
{
defaults.add(obj.getBeanClassName());
}
else
{
valid.put(obj.getBeanClassName(), match);
}
}

if (invalid.size() > 0)
{
print(\"The following shared beans have NO rules defined:\");
for (Object obj : invalid)
{
print(obj);
}
}

if (defaults.size() > 0)
{
print(\"The following shared beans have NO rules defined but are handled by the catch-all:\");
for (Object obj : defaults)
{
print(obj);
}
}

if (valid.size() > 0)
{
print(\"\\nThe following shared beans have rules defined:\");
for (Object obj : valid.entrySet())
{
rule = obj.getValue();
print(obj.getKey() + \" -> [Match=\" + rule.getName() + \", Destination=\" + rule.getPartitionId());
}
}") b)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This script will display all of the project version information for the running application ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.core.bean.ScriptHome.class) (let ((b (com.redknee.framework.core.bean.Script.))) (setBeanProperty b "Name" "Project Versions")(setBeanProperty b "Script" "
out = ctx.get(PrintWriter.class);
output = new java.util.ArrayList();
files = new java.io.File(System.getProperty(\"rk.home\") + java.io.File.separator + \"lib\").listFiles();
for (java.io.File file : files)
{
    if (file.getName().startsWith(\"rk\"))
    {
        try
        {
            s = new java.io.BufferedInputStream(new URL(\"jar:file:\" + file.getPath() + \"!/META-INF/project.xml\").openStream());
            p = new StringBuilder();
            nextByte = s.read();
            state = 0;
            while (nextByte != -1)
            {
                c = (char) nextByte;
                p.append(c);
                
                if ('<' == c) state = 1;
                else if (state == 1 && '/' == c) state = 2;
                else if (state == 2 && 'p' == c) break;
                else state = 0;

                nextByte = s.read();
            }

            name = p.substring(p.indexOf(\"<name>\") + 6, p.indexOf(\"</name>\"));
            view = p.substring(p.indexOf(\"<repositoryView>\") + 16, p.indexOf(\"</repositoryView>\"));

            label = \"\";
            start = p.indexOf(\"<label>\");
            end = p.indexOf(\"</label>\");
            if (start > -1 && end > -1)
            {
                label = p.substring(start + 7, end);
            }

            revision = \"\";
            start = p.indexOf(\"<revision>\");
            end = p.indexOf(\"</revision>\");
            if (start > -1 && end > -1)
            {
                revision = p.substring(start + 10, end);
            }

            output.add(name + \",\" + (\"0\".equals(view) ? \"trunk\" : (\"1\".equals(view) ? \"branch\" : \"tag\")) + \",\"
                    + label + \",\" + (\"0\".equals(revision) ? \" \" : revision));
        }
        catch (Exception e)
        {
            out.println(e.getClass().getName() + \": \" + e.getMessage());
            out.println(\"\");
        }
    }
}

out.print(\"<table border='1'><tr><th>Project</th><th>Repo</th><th>Label</th><th>Revision</th></tr>\");
java.util.Collections.sort(output);
for (Object item : output)
{
    out.print(\"<tr>\");
    tokens = item.split(\",\");
    for (Object token : tokens)
    {
        out.print(\"<td>\" + token + \"</td>\");
    }
    out.print(\"</tr>\");
}
out.print(\"</table>\");") b)))