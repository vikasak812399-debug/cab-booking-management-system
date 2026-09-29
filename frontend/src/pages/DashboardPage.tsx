import { useEffect, useState } from 'react';
import { apiRequest } from '../lib/api';
import { projectConfig } from '../project.config';
import { DomainVector } from '../theme/vectors';

interface StatsPayload {
  categories: number;
  departments: number;
  records: number;
  transactions: number;
}

interface ModulesPayload {
  modules: string[];
}

export function DashboardPage() {
  const [stats, setStats] = useState<StatsPayload | null>(null);
  const [modules, setModules] = useState<string[]>([]);

  useEffect(() => {
    void (async () => {
      const [statsData, modulesData] = await Promise.all([
        apiRequest<StatsPayload>('/reports/summary'),
        apiRequest<ModulesPayload>('/dashboard/modules')
      ]);
      setStats(statsData);
      setModules(modulesData.modules);
    })();
  }, []);

  return (
    <section>
      <div className="mb-6 grid gap-5 lg:grid-cols-[1fr,300px]">
        <div>
          <h2 className="text-2xl font-semibold">{projectConfig.dashboardTitle || 'Dashboard Stats'}</h2>
          <p className="mt-2 text-sm text-slate-600">{projectConfig.recordPage.intro}</p>
        </div>
        <DomainVector className="hidden w-full lg:block" />
      </div>
      {projectConfig.majorProject ? (
        <div className="theme-card mb-5 p-4">
          <p className="text-xs font-semibold uppercase text-sky-700">{projectConfig.majorCategory}</p>
          <h3 className="mt-1 text-lg font-semibold text-slate-900">{projectConfig.complexity} project profile</h3>
          <p className="mt-1 text-sm text-slate-700">{projectConfig.majorProject.problemDepth}</p>
        </div>
      ) : null}
      <div className="grid grid-cols-2 gap-3 md:grid-cols-4">
        {projectConfig.dashboardCards.map((card) => (
          <Card key={card.title} title={card.title} value={stats?.[card.key as keyof StatsPayload] ?? 0} helper={card.helper} />
        ))}
      </div>

      <h3 className="mt-8 text-lg font-semibold">Unique Modules</h3>
      <ul className="mt-2 list-disc pl-6 text-sm text-slate-700">
        {modules.map((item) => (
          <li key={item}>{item}</li>
        ))}
      </ul>
      {projectConfig.majorProject ? (
        <div className="mt-8 grid gap-4 md:grid-cols-2">
          <section>
            <h3 className="text-lg font-semibold">Major Project Modules</h3>
            <ul className="mt-2 list-disc pl-6 text-sm text-slate-700">
              {projectConfig.majorProject.advancedModules.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </section>
          <section>
            <h3 className="text-lg font-semibold">Advanced Automation Scope</h3>
            <ul className="mt-2 list-disc pl-6 text-sm text-slate-700">
              {projectConfig.majorProject.aiUseCases.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </section>
        </div>
      ) : null}
    </section>
  );
}

function Card({ title, value, helper }: { title: string; value: number; helper: string }) {
  return (
    <article className="theme-card p-4">
      <p className="text-sm text-slate-500">{title}</p>
      <p className="text-2xl font-bold">{value}</p>
      <p className="mt-1 text-xs text-slate-500">{helper}</p>
    </article>
  );
}
